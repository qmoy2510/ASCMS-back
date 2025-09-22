package com.example.afterSchool.dto;

import com.example.afterSchool.entity.enums.DayOfWeekEnum;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AfterSchoolClassResponse {
    private Integer classId;
    private Integer teacherId;
    private String title;
    private String description;
    private DayOfWeekEnum dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer capacity;
}
