package com.example.afterSchool.repository;

import com.example.afterSchool.entity.AfterSchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AfterSchoolClassRepository extends JpaRepository<AfterSchoolClass, Integer> {
    List<AfterSchoolClass> findByTeacher_TeacherId(Integer teacherId);
}