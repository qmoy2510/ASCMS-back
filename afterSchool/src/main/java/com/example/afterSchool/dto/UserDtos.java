package com.example.afterSchool.dto;

import lombok.Data;

public class UserDtos {

    @Data
    public static class RegistrationRequest {
        private String email;
        private String password;
        private String name;
        // 학생증 또는 교직원 번호 등 인증 정보를 추가할 수 있습니다.
        // private String certificationNumber;
    }

    @Data
    public static class UserResponse {
        private Integer id;
        private String email;
        private String name;
        private String userType; // "STUDENT" or "TEACHER"
    }
}