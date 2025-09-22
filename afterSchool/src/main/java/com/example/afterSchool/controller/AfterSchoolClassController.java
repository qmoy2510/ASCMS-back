package com.example.afterSchool.controller;

import com.example.afterSchool.dto.AfterSchoolClassRequest;
import com.example.afterSchool.dto.AfterSchoolClassResponse;
import com.example.afterSchool.service.AfterSchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class AfterSchoolClassController {

    private final AfterSchoolClassService classService;

    @GetMapping
    public ResponseEntity<List<AfterSchoolClassResponse>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AfterSchoolClassResponse> getClass(@PathVariable Integer id) {
        return ResponseEntity.ok(classService.getClassById(id));
    }

    @PostMapping
    public ResponseEntity<AfterSchoolClassResponse> createClass(@RequestBody AfterSchoolClassRequest request) {
        return ResponseEntity.ok(classService.createClass(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AfterSchoolClassResponse> updateClass(
            @PathVariable Integer id,
            @RequestBody AfterSchoolClassRequest request) {
        return ResponseEntity.ok(classService.updateClass(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Integer id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}
