package com.example.afterSchool.controller;

import com.example.afterSchool.dto.*;
import com.example.afterSchool.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/register") // tc-001
    public ResponseEntity<Void> registerTeacher(@RequestBody UserDtos.RegistrationRequest request) {
        teacherService.registerTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/classes") // tc-002
    public ResponseEntity<Void> createClass(@RequestBody ClassDtos.ClassCreationRequest request) {
        teacherService.createClass(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/notices") // tc-003
    public ResponseEntity<Void> createNotice(@RequestBody NoticeDtos.NoticeRequest request) {
        teacherService.createNotice(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/classes/{classId}") // tc-004
    public ResponseEntity<Void> updateClass(@PathVariable Integer classId, @RequestBody ClassDtos.ClassCreationRequest request) {
        teacherService.updateClass(classId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/attendance") // tc-005
    public ResponseEntity<Void> recordAttendance(@RequestBody AttendanceDtos.AttendanceRequest request) {
        teacherService.recordAttendance(request);
        return ResponseEntity.ok().build();
    }
}