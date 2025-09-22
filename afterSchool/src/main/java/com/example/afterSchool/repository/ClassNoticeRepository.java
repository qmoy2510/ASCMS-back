package com.example.afterSchool.repository;

import com.example.afterSchool.entity.ClassNotice;
import com.example.afterSchool.entity.AfterSchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassNoticeRepository extends JpaRepository<ClassNotice, Integer> {
    List<ClassNotice> findByAfterSchoolClass(AfterSchoolClass afterSchoolClass);
}
