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
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attendanceId;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    private LocalDate classDate;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private LocalDateTime recordedAt;
}
