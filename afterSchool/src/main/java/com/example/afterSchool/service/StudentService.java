package com.example.afterSchool.service;

import com.example.afterSchool.dto.ClassDtos;
import com.example.afterSchool.dto.EnrollmentDtos;
import com.example.afterSchool.dto.UserDtos;
import com.example.afterSchool.entity.*;
import com.example.afterSchool.entity.enums.EnrollmentStatus;
import com.example.afterSchool.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final AfterSchoolClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;

    // (sd-001) 학생 회원가입
    @Transactional
    public Student registerStudent(UserDtos.RegistrationRequest request) {
        if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists.");
        }
        Student student = new Student();
        student.setEmail(request.getEmail());
        // 실제로는 BCryptPasswordEncoder 등을 사용해 비밀번호를 해싱해야 합니다.
        student.setPasswordHash(request.getPassword());
        student.setName(request.getName());
        return studentRepository.save(student);
    }

    // (sd-002) 자신의 방과후 일정 달력 형식으로 확인
    public List<EnrollmentDtos.CalendarResponse> getStudentSchedule(Integer studentId) {
        return enrollmentRepository.findByStudent_StudentId(studentId).stream()
                .map(enrollment -> {
                    AfterSchoolClass cls = enrollment.getAfterSchoolClass();
                    EnrollmentDtos.CalendarResponse cal = new EnrollmentDtos.CalendarResponse();
                    cal.setTitle(cls.getTitle());
                    cal.setDayOfWeek(cls.getDayOfWeek().name());
                    // 실제 달력에 표시하려면 구체적인 날짜 계산 로직이 필요합니다.
                    // 여기서는 요일과 시간 정보만 제공합니다.
                    // cal.setStart(...)
                    // cal.setEnd(...)
                    return cal;
                })
                .collect(Collectors.toList());
    }

    // (sd-003) 자신이 수강 중인 방과후 목록 (출석률 포함) 확인
    public List<EnrollmentDtos.EnrolledClassResponse> getEnrolledClasses(Integer studentId) {
        return enrollmentRepository.findByStudent_StudentId(studentId).stream()
                .map(e -> {
                    EnrollmentDtos.EnrolledClassResponse dto = new EnrollmentDtos.EnrolledClassResponse();
                    dto.setEnrollmentId(e.getEnrollmentId());
                    dto.setClassTitle(e.getAfterSchoolClass().getTitle());
                    dto.setTeacherName(e.getAfterSchoolClass().getTeacher().getName());
                    dto.setAttendanceRate(e.getAttendanceRate());
                    dto.setAppliedAt(e.getAppliedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // (sd-004) 신청할 수 있는 방과후 목록 확인
    public List<ClassDtos.ClassResponse> getAvailableClasses(Integer studentId) {
        // 1. 출석률 60% 미만 과목 확인
        boolean hasLowAttendance = enrollmentRepository.findByStudent_StudentId(studentId).stream()
                .anyMatch(e -> e.getAttendanceRate() != null && e.getAttendanceRate().compareTo(new BigDecimal("60")) < 0);

        if (hasLowAttendance) {
            throw new IllegalStateException("신청 기능이 잠겼습니다. (출석률 60% 미만 과목 존재)");
        }

        // 2. 전체 방과후 목록에서 이미 신청했거나, 정원이 찼거나, 요일이 겹치는 과목 제외
        // ... (필터링 로직 구현) ...
        return classRepository.findAll().stream()
                // ...
                .map(c -> { /* DTO 변환 */ return new ClassDtos.ClassResponse(); })
                .collect(Collectors.toList());
    }


    // (sd-005) 방과후 신청
    @Transactional
    public Enrollment enrollInClass(EnrollmentDtos.EnrollmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId()).orElseThrow();
        AfterSchoolClass aClass = classRepository.findById(request.getClassId()).orElseThrow();

        // 1. 출석률 60% 미만 과목 확인 (getAvailableClasses 로직과 동일)
        // ...

        // 2. 요일 중복 확인
        boolean timeConflict = enrollmentRepository.existsByStudentAndAfterSchoolClass_DayOfWeek(student, aClass.getDayOfWeek());
        if (timeConflict) {
            throw new IllegalStateException("요일이 완전히 겹치는 방과후는 신청할 수 없습니다.");
        }

        // 3. 정원 확인
        long currentEnrolled = enrollmentRepository.countByAfterSchoolClass_ClassId(aClass.getClassId());
        if (currentEnrolled >= aClass.getCapacity()) {
            throw new IllegalStateException("정원이 모두 찼습니다.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setAfterSchoolClass(aClass);
        enrollment.setStatus(EnrollmentStatus.enrolled);
        // ...
        return enrollmentRepository.save(enrollment);
    }
}