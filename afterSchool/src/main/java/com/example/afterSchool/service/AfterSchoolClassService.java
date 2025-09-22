package com.example.afterSchool.service;

import com.example.afterSchool.dto.AfterSchoolClassRequest;
import com.example.afterSchool.dto.AfterSchoolClassResponse;
import com.example.afterSchool.entity.AfterSchoolClass;
import com.example.afterSchool.entity.Teacher;
import com.example.afterSchool.repository.AfterSchoolClassRepository;
import com.example.afterSchool.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AfterSchoolClassService {

    private final AfterSchoolClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    // 목록 조회
    public List<AfterSchoolClassResponse> getAllClasses() {
        return classRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 단일 조회
    public AfterSchoolClassResponse getClassById(Integer classId) {
        return classRepository.findById(classId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Class not found"));
    }

    // 등록
    public AfterSchoolClassResponse createClass(AfterSchoolClassRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        AfterSchoolClass afterSchoolClass = new AfterSchoolClass();
        afterSchoolClass.setTeacher(teacher);
        afterSchoolClass.setTitle(request.getTitle());
        afterSchoolClass.setDescription(request.getDescription());
        afterSchoolClass.setDayOfWeek(request.getDayOfWeek());
        afterSchoolClass.setStartTime(request.getStartTime());
        afterSchoolClass.setEndTime(request.getEndTime());
        afterSchoolClass.setCapacity(request.getCapacity());

        AfterSchoolClass saved = classRepository.save(afterSchoolClass);
        return toResponse(saved);
    }

    // 수정
    public AfterSchoolClassResponse updateClass(Integer classId, AfterSchoolClassRequest request) {
        AfterSchoolClass existing = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        existing.setTeacher(teacher);
        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setDayOfWeek(request.getDayOfWeek());
        existing.setStartTime(request.getStartTime());
        existing.setEndTime(request.getEndTime());
        existing.setCapacity(request.getCapacity());

        return toResponse(classRepository.save(existing));
    }

    // 삭제
    public void deleteClass(Integer classId) {
        classRepository.deleteById(classId);
    }

    // 엔티티 → DTO 변환
    private AfterSchoolClassResponse toResponse(AfterSchoolClass cls) {
        return new AfterSchoolClassResponse(
                cls.getClassId(),
                cls.getTeacher().getTeacherId(),
                cls.getTitle(),
                cls.getDescription(),
                cls.getDayOfWeek(),
                cls.getStartTime(),
                cls.getEndTime(),
                cls.getCapacity()
        );
    }
}
