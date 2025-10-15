package com.example.afterSchool.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EnrollmentDtos {

    @Data
    public static class EnrollmentRequest {
        private Integer studentId;
        private Integer classId;
    }

    @Data
    public static class EnrolledClassResponse {
        private Integer enrollmentId;
        private String classTitle;
        private String teacherName;
        private BigDecimal attendanceRate;
        private LocalDateTime appliedAt;
    }

    @Data
    public static class CalendarResponse {
        private String title;
        private String start; // 예: "2025-09-29T14:00:00"
        private String end;   // 예: "2025-09-29T16:00:00"
        private String dayOfWeek; // Mon, Tue ...
    }
}