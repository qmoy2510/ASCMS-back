package com.example.afterSchool.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "class_notices")
// 👈 시퀀스 생성기 추가
@SequenceGenerator(
        name = "NOTICE_SEQ_GENERATOR",
        sequenceName = "NOTICE_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class ClassNotice {
    @Id
    // 👈 ID 생성 전략 변경
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTICE_SEQ_GENERATOR")
    private Integer noticeId;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private AfterSchoolClass afterSchoolClass;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
}