package com.example.afterSchool.service;

import com.example.afterSchool.dto.user.UserDto;
import com.example.afterSchool.repository.AdminRepository;
import com.example.afterSchool.repository.StudentRepository;
import com.example.afterSchool.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;

    public List<UserDto.StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(UserDto.StudentResponse::from)
                .collect(Collectors.toList());
    }

    public List<UserDto.TeacherResponse> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(UserDto.TeacherResponse::from)
                .collect(Collectors.toList());
    }

    public List<UserDto.AdminResponse> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(UserDto.AdminResponse::from)
                .collect(Collectors.toList());
    }
}