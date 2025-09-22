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
public class AfterSchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
