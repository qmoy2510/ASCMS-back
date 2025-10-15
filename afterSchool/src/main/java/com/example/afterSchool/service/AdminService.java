package com.example.afterSchool.service;

import com.example.afterSchool.dto.ClassDtos;
import com.example.afterSchool.dto.UserDtos;
import com.example.afterSchool.repository.AfterSchoolClassRepository;
import com.example.afterSchool.repository.StudentRepository;
import com.example.afterSchool.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AfterSchoolClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    // (sy-001) 방과후 목록 확인
    public List<ClassDtos.ClassResponse> getAllClasses() {
        return classRepository.findAll().stream()
                .map(c -> {
                    ClassDtos.ClassResponse dto = new ClassDtos.ClassResponse();
                    // ... DTO 변환 로직 ...
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // (sy-002) 방과후 강제 삭제
    @Transactional
    public void deleteClass(Integer classId) {
        // 관련 enrollment, attendance 등 종속된 데이터를 먼저 삭제해야 합니다. (Cascading 설정 또는 직접 삭제)
        classRepository.deleteById(classId);
    }

    // (sy-003) 모든 회원 목록 확인
    public List<UserDtos.UserResponse> getAllUsers() {
        List<UserDtos.UserResponse> students = studentRepository.findAll().stream()
                .map(s -> {
                    UserDtos.UserResponse dto = new UserDtos.UserResponse();
                    dto.setId(s.getStudentId());
                    dto.setEmail(s.getEmail());
                    dto.setName(s.getName());
                    dto.setUserType("STUDENT");
                    return dto;
                })
                .collect(Collectors.toList());

        List<UserDtos.UserResponse> teachers = teacherRepository.findAll().stream()
                .map(t -> {
                    UserDtos.UserResponse dto = new UserDtos.UserResponse();
                    dto.setId(t.getTeacherId());
                    dto.setEmail(t.getEmail());
                    dto.setName(t.getName());
                    dto.setUserType("TEACHER");
                    return dto;
                })
                .collect(Collectors.toList());

        return Stream.concat(students.stream(), teachers.stream()).collect(Collectors.toList());
    }

    // (sy-004) 회원 삭제
    @Transactional
    public void deleteUser(Integer userId, String userType) {
        if ("STUDENT".equalsIgnoreCase(userType)) {
            studentRepository.deleteById(userId);
        } else if ("TEACHER".equalsIgnoreCase(userType)) {
            teacherRepository.deleteById(userId);
        } else {
            throw new IllegalArgumentException("Invalid user type.");
        }
    }

    // (sy-005) 회원 직급 변경: 학생 -> 교사 (또는 반대)
    // 주의: 이 기능은 단순히 role 필드를 바꾸는 것이 아니라 한 테이블에서 삭제하고 다른 테이블에 생성해야 하므로
    // 데이터 유실 위험이 있습니다. 실제 구현 시에는 신중한 설계가 필요합니다.
    @Transactional
    public void changeUserRole(Integer userId, String fromType, String toType) {
        // 이 로직은 복잡하며, 사용자 정보(비밀번호 등)를 어떻게 이전할지에 대한 정책이 필요합니다.
        // 예시: 학생을 교사로 변경
        if ("STUDENT".equalsIgnoreCase(fromType) && "TEACHER".equalsIgnoreCase(toType)) {
            studentRepository.findById(userId).ifPresent(student -> {
                // Teacher 객체 생성 및 정보 복사
                // student 삭제
            });
        }
        // ...
    }
}