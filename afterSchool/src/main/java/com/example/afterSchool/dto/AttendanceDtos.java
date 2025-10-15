package com.example.afterSchool.dto;

import com.example.afterSchool.entity.Attendance;
import lombok.Data;
import java.time.LocalDate;

public class AttendanceDtos {
    @Data
    public static class AttendanceRequest {
        private Integer enrollmentId;
        private LocalDate classDate;
        private Attendance status;
    }
}