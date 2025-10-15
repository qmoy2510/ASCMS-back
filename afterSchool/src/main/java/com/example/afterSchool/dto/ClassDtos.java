package com.example.afterSchool.dto;

import com.example.afterSchool.entity.enums.DayOfWeekEnum;
import lombok.Data;
import java.time.LocalTime;

public class ClassDtos {

    @Data
    public static class ClassCreationRequest {
        private Integer teacherId;
        private String title;
        private String description;
        private DayOfWeekEnum dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer capacity;
    }

    @Data
    public static class ClassResponse {
        private Integer classId;
        private String teacherName;
        private String title;
        private DayOfWeekEnum dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer capacity;
        private long currentEnrollment;
    }
}