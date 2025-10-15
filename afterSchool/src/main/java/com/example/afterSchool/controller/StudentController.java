package com.example.afterSchool.controller;

import com.example.afterSchool.dto.*;
import com.example.afterSchool.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register") // sd-001
    public ResponseEntity<Void> registerStudent(@RequestBody UserDtos.RegistrationRequest request) {
        studentService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{studentId}/schedule") // sd-002
    public ResponseEntity<List<EnrollmentDtos.CalendarResponse>> getStudentSchedule(@PathVariable Integer studentId) {
        return ResponseEntity.ok(studentService.getStudentSchedule(studentId));
    }

    @GetMapping("/{studentId}/enrolled-classes") // sd-003
    public ResponseEntity<List<EnrollmentDtos.EnrolledClassResponse>> getEnrolledClasses(@PathVariable Integer studentId) {
        return ResponseEntity.ok(studentService.getEnrolledClasses(studentId));
    }

    @GetMapping("/{studentId}/available-classes") // sd-004
    public ResponseEntity<List<ClassDtos.ClassResponse>> getAvailableClasses(@PathVariable Integer studentId) {
        return ResponseEntity.ok(studentService.getAvailableClasses(studentId));
    }

    @PostMapping("/enroll") // sd-005
    public ResponseEntity<Void> enrollInClass(@RequestBody EnrollmentDtos.EnrollmentRequest request) {
        studentService.enrollInClass(request);
        return ResponseEntity.ok().build();
    }
}