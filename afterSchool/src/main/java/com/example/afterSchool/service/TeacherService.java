package com.example.afterSchool.service;

import com.example.afterSchool.dto.AttendanceDtos;
import com.example.afterSchool.dto.ClassDtos;
import com.example.afterSchool.dto.NoticeDtos;
import com.example.afterSchool.dto.UserDtos;
import com.example.afterSchool.entity.*;
import com.example.afterSchool.entity.enums.AttendanceStatus;
import com.example.afterSchool.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository; // 이메일 중복 확인을 위해 추가
    private final AfterSchoolClassRepository classRepository;
    private final ClassNoticeRepository noticeRepository;
    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;

    /**
     * (tc-001) 교육자 회원가입
     */
    @Transactional
    public Teacher registerTeacher(UserDtos.RegistrationRequest request) {
        // 학생과 교사를 통틀어 이메일이 중복되는지 확인
        if (teacherRepository.findByEmail(request.getEmail()).isPresent() ||
                studentRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        Teacher teacher = new Teacher();
        teacher.setEmail(request.getEmail());
        teacher.setName(request.getName());
        // TODO: 실제 서비스에서는 반드시 비밀번호를 암호화해야 합니다.
        // teacher.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        teacher.setPasswordHash(request.getPassword()); // 임시로 평문 저장
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        return teacherRepository.save(teacher);
    }

    /**
     * (tc-002) 담당 방과후 등록
     */
    @Transactional
    public AfterSchoolClass createClass(ClassDtos.ClassCreationRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("해당 교사를 찾을 수 없습니다."));

        AfterSchoolClass newClass = new AfterSchoolClass();
        newClass.setTeacher(teacher);
        newClass.setTitle(request.getTitle());
        newClass.setDescription(request.getDescription());
        newClass.setDayOfWeek(request.getDayOfWeek());
        newClass.setStartTime(request.getStartTime());
        newClass.setEndTime(request.getEndTime());
        newClass.setCapacity(request.getCapacity());
        newClass.setCreatedAt(LocalDateTime.now());

        return classRepository.save(newClass);
    }

    /**
     * (tc-003) 방과후 수업 공지
     */
    @Transactional
    public ClassNotice createNotice(NoticeDtos.NoticeRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("해당 교사를 찾을 수 없습니다."));
        AfterSchoolClass aClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("해당 방과후 수업을 찾을 수 없습니다."));

        ClassNotice notice = new ClassNotice();
        notice.setTeacher(teacher);
        notice.setAfterSchoolClass(aClass);
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setCreatedAt(LocalDateTime.now());

        return noticeRepository.save(notice);
    }

    /**
     * (tc-004) 방과후 수정 (특정 기간)
     */
    @Transactional
    public AfterSchoolClass updateClass(Integer classId, ClassDtos.ClassCreationRequest request) {
        AfterSchoolClass aClass = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방과후 수업을 찾을 수 없습니다."));

        // 예시: 생성 후 7일 이내에만 수정 가능
        if (aClass.getCreatedAt().isBefore(LocalDateTime.now().minusDays(7))) {
            throw new IllegalStateException("수정 가능한 기간이 지났습니다. 관리자에게 문의하세요.");
        }

        aClass.setTitle(request.getTitle());
        aClass.setDescription(request.getDescription());
        aClass.setDayOfWeek(request.getDayOfWeek());
        aClass.setStartTime(request.getStartTime());
        aClass.setEndTime(request.getEndTime());
        aClass.setCapacity(request.getCapacity());

        return classRepository.save(aClass);
    }

    /**
     * (tc-005) 학생 출석 관리 및 출석률 자동 업데이트
     */
    @Transactional
    public Attendance recordAttendance(AttendanceDtos.AttendanceRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("해당 수강 정보를 찾을 수 없습니다."));

        Attendance attendance = new Attendance();
        attendance.setEnrollment(enrollment);
        attendance.setClassDate(request.getClassDate());
        attendance.setStatus(request.getStatus().getStatus());
        attendance.setRecordedAt(LocalDateTime.now());

        Attendance savedAttendance = attendanceRepository.save(attendance);

        // 출석률 자동 재계산 및 업데이트
        updateAttendanceRate(enrollment);

        return savedAttendance;
    }

    /**
     * 특정 수강 정보에 대한 출석률을 계산하고 업데이트하는 private 헬퍼 메소드
     */
    private void updateAttendanceRate(Enrollment enrollment) {
        List<Attendance> allAttendances = attendanceRepository.findByEnrollment_EnrollmentId(enrollment.getEnrollmentId());

        if (allAttendances.isEmpty()) {
            enrollment.setAttendanceRate(BigDecimal.ZERO);
            enrollmentRepository.save(enrollment);
            return;
        }

        long presentOrLateCount = allAttendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.present || a.getStatus() == AttendanceStatus.late)
                .count();

        BigDecimal total = new BigDecimal(allAttendances.size());
        BigDecimal present = new BigDecimal(presentOrLateCount);

        // 출석률 계산: (출석인정 / 전체) * 100, 소수점 4자리에서 반올림하여 2자리까지 표시
        BigDecimal rate = present.divide(total, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        enrollment.setAttendanceRate(rate.setScale(2, RoundingMode.HALF_UP));

        enrollmentRepository.save(enrollment);
    }
}