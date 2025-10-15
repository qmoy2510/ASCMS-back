package com.example.afterSchool.repository;

import com.example.afterSchool.entity.ClassNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassNoticeRepository extends JpaRepository<ClassNotice, Integer> {
}