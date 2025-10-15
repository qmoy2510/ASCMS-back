package com.example.afterSchool.dto;

import lombok.Data;

public class NoticeDtos {
    @Data
    public static class NoticeRequest {
        private Integer teacherId;
        private Integer classId;
        private String title;
        private String content;
    }
}