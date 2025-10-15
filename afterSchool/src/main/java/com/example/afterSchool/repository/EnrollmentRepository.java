package com.example.afterSchool.repository;

import com.example.afterSchool.entity.Enrollment;
import com.example.afterSchool.entity.Student;
import com.example.afterSchool.entity.enums.DayOfWeekEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudent_StudentId(Integer studentId);
    boolean existsByStudentAndAfterSchoolClass_DayOfWeek(Student student, DayOfWeekEnum dayOfWeek);
    long countByAfterSchoolClass_ClassId(Integer classId);
}