package com.example.afterSchool.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "afterschool_classes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AfterschoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;
    private Integer capacity;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "clazz")
    private List<ClassEnrollment> enrollments;

    @OneToMany(mappedBy = "clazz")
    private List<ClassNotice> notices;

    public enum DayOfWeek { Mon, Tue, Wed, Thu, Fri, Sat }
}
