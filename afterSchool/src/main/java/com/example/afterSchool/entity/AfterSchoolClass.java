package com.example.afterSchool.entity;

import com.example.afterSchool.entity.enums.DayOfWeekEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "afterschool_classes")
// 👈 시퀀스 생성기 추가
@SequenceGenerator(
        name = "CLASS_SEQ_GENERATOR",
        sequenceName = "CLASS_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class AfterSchoolClass {
    @Id
    // 👈 ID 생성 전략 변경
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLASS_SEQ_GENERATOR")
    private Integer classId;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private DayOfWeekEnum dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer capacity;

    private LocalDateTime createdAt;
}