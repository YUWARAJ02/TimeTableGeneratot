package com.timetable.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSubjectsDTO {
    
    private String teacherName;

    private String subjects;
}
