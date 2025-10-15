package com.example.afterSchool.controller;

import com.example.afterSchool.dto.ClassDtos;
import com.example.afterSchool.dto.UserDtos;
import com.example.afterSchool.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/classes") // sy-001
    public ResponseEntity<List<ClassDtos.ClassResponse>> getAllClasses() {
        return ResponseEntity.ok(adminService.getAllClasses());
    }

    @DeleteMapping("/classes/{classId}") // sy-002
    public ResponseEntity<Void> deleteClass(@PathVariable Integer classId) {
        adminService.deleteClass(classId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users") // sy-003
    public ResponseEntity<List<UserDtos.UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{userType}/{userId}") // sy-004
    public ResponseEntity<Void> deleteUser(@PathVariable String userType, @PathVariable Integer userId) {
        adminService.deleteUser(userId, userType);
        return ResponseEntity.ok().build();
    }
}