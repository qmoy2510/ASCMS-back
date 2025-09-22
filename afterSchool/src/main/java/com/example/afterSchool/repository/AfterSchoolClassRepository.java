package com.example.afterSchool.repository;

import com.example.afterSchool.entity.AfterSchoolClass;
import com.example.afterSchool.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AfterSchoolClassRepository extends JpaRepository<AfterSchoolClass, Integer> {
    List<AfterSchoolClass> findByTeacher(Teacher teacher);
}
