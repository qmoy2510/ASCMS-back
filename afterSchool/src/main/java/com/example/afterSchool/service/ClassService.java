package com.example.afterSchool.service;

import com.example.afterSchool.dto.clazz.ClassCreateRequest;
import com.example.afterSchool.dto.clazz.ClassResponse;
import com.example.afterSchool.dto.user.UserDto;
import com.example.afterSchool.entity.*;
import com.example.afterSchool.entity.enums.EnrollmentStatus;
import com.example.afterSchool.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassService {

    private final AfterSchoolClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ClassEnrollmentRepository enrollmentRepository;
    // ClassScheduleRepository는 CascadeType.ALL 때문에 직접 호출 안 해도 됨

    // 1. 전체 수업 리스트 조회 (현재 인원 포함)
    public List<ClassResponse> getAllClasses() {
        return classRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(clazz -> {
                    // 수강 인원 계산 (status가 ENROLLED인 것만)
                    int currentCount = (int) enrollmentRepository.findAll().stream()
                            .filter(e -> e.getAfterSchoolClass().getId().equals(clazz.getId())
                                    && e.getStatus() == EnrollmentStatus.ENROLLED)
                            .count();
                    // 참고: Repository에 countByAfterSchoolClassAndStatus가 있다면 더 효율적
                    return ClassResponse.from(clazz, currentCount);
                })
                .collect(Collectors.toList());
    }

    // 2. 수업 개설
    @Transactional
    public void createClass(String teacherEmail, ClassCreateRequest req) {
        Teacher teacher = teacherRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("교사 정보를 찾을 수 없습니다."));

        AfterSchoolClass newClass = AfterSchoolClass.builder()
                .teacher(teacher)
                .title(req.getTitle())
                .description(req.getDescription())
                .capacity(req.getCapacity())
                .classLocation(req.getClassLocation())
                .build();

        // 시간표 매핑 및 추가 (Entity에 getSchedules().add() 로직 사용)
        if (req.getSchedules() != null) {
            List<ClassSchedule> schedules = req.getSchedules().stream()
                    .map(s -> ClassSchedule.builder()
                            .afterSchoolClass(newClass)
                            .dayOfWeek(s.getDayOfWeek())
                            .startTime(s.getStartTime())
                            .endTime(s.getEndTime())
                            .build())
                    .collect(Collectors.toList());

            // AfterSchoolClass 엔티티에 schedules 필드는 있지만 add 메서드가 없다면
            // getSchedules().addAll(schedules)로 처리 (단, 리스트가 초기화되어 있어야 함)
            newClass.getSchedules().addAll(schedules);
        }

        classRepository.save(newClass);
    }

    // 3. 수강 신청
    @Transactional
    public void enrollClass(String studentEmail, Long classId) {
        Student student = studentRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("학생 정보를 찾을 수 없습니다."));
        AfterSchoolClass clazz = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("수업을 찾을 수 없습니다."));

        // 중복 신청 체크
        boolean alreadyEnrolled = enrollmentRepository.findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ENROLLED).stream()
                .anyMatch(e -> e.getAfterSchoolClass().getId().equals(classId));

        if (alreadyEnrolled) {
            throw new RuntimeException("이미 신청한 수업입니다.");
        }

        // 정원 초과 체크
        long currentCount = enrollmentRepository.findByAfterSchoolClassId(classId).stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                .count();

        if (currentCount >= clazz.getCapacity()) {
            throw new RuntimeException("정원이 초과되었습니다.");
        }

        // 신청 저장
        ClassEnrollment enrollment = ClassEnrollment.builder()
                .student(student)
                .afterSchoolClass(clazz)
                .status(EnrollmentStatus.ENROLLED)
                .build();

        enrollmentRepository.save(enrollment);
    }

    // 4. 학생의 수업 조회
    public List<ClassResponse> getMyClassesForStudent(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow();

        return enrollmentRepository.findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ENROLLED).stream()
                .map(enrollment -> {
                    AfterSchoolClass clazz = enrollment.getAfterSchoolClass();
                    // 인원 계산
                    long count = enrollmentRepository.findByAfterSchoolClassId(clazz.getId()).stream()
                            .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED).count();
                    return ClassResponse.from(clazz, (int) count);
                })
                .collect(Collectors.toList());
    }

    // 5. 교사의 수업 조회
    public List<ClassResponse> getMyClassesForTeacher(String email) {
        Teacher teacher = teacherRepository.findByEmail(email).orElseThrow();

        return classRepository.findByTeacherId(teacher.getId()).stream()
                .map(clazz -> {
                    long count = enrollmentRepository.findByAfterSchoolClassId(clazz.getId()).stream()
                            .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED).count();
                    return ClassResponse.from(clazz, (int) count);
                })
                .collect(Collectors.toList());
    }

    // 6. 특정 수업의 학생 리스트 조회
    public List<UserDto.StudentResponse> getStudentsInClass(Long classId) {
        return enrollmentRepository.findByAfterSchoolClassId(classId).stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                .map(e -> UserDto.StudentResponse.from(e.getStudent()))
                .collect(Collectors.toList());
    }

    // 7. 수업 삭제
    @Transactional
    public void deleteClass(Long classId) {
        // Cascade 설정으로 인해 Enrollments, Schedules 등도 같이 삭제될 수 있음 (Entity 설정 확인 필요)
        classRepository.deleteById(classId);
    }
}