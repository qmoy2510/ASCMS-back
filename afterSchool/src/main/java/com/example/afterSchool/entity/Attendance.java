package com.example.afterSchool.entity;

import com.example.afterSchool.entity.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "class_attendance")
// 👈 시퀀스 생성기 추가
@SequenceGenerator(
        name = "ATTENDANCE_SEQ_GENERATOR",
        sequenceName = "ATTENDANCE_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Attendance {
    @Id
    // 👈 ID 생성 전략 변경
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTENDANCE_SEQ_GENERATOR")
    private Integer attendanceId;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    private LocalDate classDate;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private LocalDateTime recordedAt;
}