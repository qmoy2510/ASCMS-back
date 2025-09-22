package com.example.afterSchool.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_attendance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClassAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private ClassEnrollment enrollment;

    private LocalDate classDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime recordedAt;

    public enum Status { present, absent, late }
}

