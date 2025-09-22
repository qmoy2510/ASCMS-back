package com.example.afterSchool.repository;

import com.example.afterSchool.entity.Enrollment;
import com.example.afterSchool.entity.Student;
import com.example.afterSchool.entity.AfterSchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findByAfterSchoolClass(AfterSchoolClass afterSchoolClass);
    Optional<Enrollment> findByStudentAndAfterSchoolClass(Student student, AfterSchoolClass afterSchoolClass);
}
