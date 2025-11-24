package com.example.afterSchool.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "afterschool_classes")
public class AfterSchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "class_location", nullable = false, length = 100)
    private String classLocation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ▼▼▼ [여기 수정!] @Builder.Default 추가 ▼▼▼
    // 이걸 붙여야 빌더 패턴을 써도 new ArrayList<>() 초기화가 유지됩니다.
    @Builder.Default
    @OneToMany(mappedBy = "afterSchoolClass", cascade = CascadeType.ALL)
    private List<ClassSchedule> schedules = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}