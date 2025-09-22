package com.example.afterSchool.repository;

import com.example.afterSchool.entity.Attendance;
import com.example.afterSchool.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByEnrollment(Enrollment enrollment);
    List<Attendance> findByClassDate(LocalDate classDate);
}
