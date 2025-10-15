package com.example.afterSchool.entity;

import com.example.afterSchool.entity.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "class_enrollments")
// 👈 시퀀스 생성기 추가
@SequenceGenerator(
        name = "ENROLLMENT_SEQ_GENERATOR",
        sequenceName = "ENROLLMENT_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Enrollment {
    @Id
    // 👈 ID 생성 전략 변경
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENROLLMENT_SEQ_GENERATOR")
    private Integer enrollmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private AfterSchoolClass afterSchoolClass;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Column(precision = 5, scale = 2)
    private BigDecimal attendanceRate;

    private LocalDateTime appliedAt;
}