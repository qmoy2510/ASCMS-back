package com.example.afterSchool.repository;

import com.example.afterSchool.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByEnrollment_EnrollmentId(Integer enrollmentId);
}