package com.example.afterSchool.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher")
// 👈 시퀀스 생성기 추가
@SequenceGenerator(
        name = "TEACHER_SEQ_GENERATOR",
        sequenceName = "TEACHER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Teacher {
    @Id
    // 👈 ID 생성 전략 변경
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEACHER_SEQ_GENERATOR")
    private Integer teacherId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 50)
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}