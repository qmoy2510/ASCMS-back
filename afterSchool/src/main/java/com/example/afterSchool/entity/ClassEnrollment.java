package com.example.afterSchool.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "class_enrollments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClassEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private AfterschoolClass clazz;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(precision = 5, scale = 2)
    private Double attendanceRate;

    private LocalDateTime appliedAt;

    @OneToMany(mappedBy = "enrollment")
    private List<ClassAttendance> attendances;

    public enum Status { enrolled, cancelled }
}
