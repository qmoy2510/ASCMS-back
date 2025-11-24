package com.example.afterSchool.controller;

import com.example.afterSchool.dto.user.UserDto;
import com.example.afterSchool.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. 전체 학생 리스트 조회
    @GetMapping("/students")
    public ResponseEntity<List<UserDto.StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }

    // 2. 전체 교사 리스트 조회
    @GetMapping("/teachers")
    public ResponseEntity<List<UserDto.TeacherResponse>> getAllTeachers() {
        return ResponseEntity.ok(userService.getAllTeachers());
    }

    // 3. 전체 관리자 리스트 조회
    @GetMapping("/admins")
    public ResponseEntity<List<UserDto.AdminResponse>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmins());
    }
}