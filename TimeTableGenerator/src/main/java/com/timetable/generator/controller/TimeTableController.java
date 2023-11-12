package com.timetable.generator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timetable.generator.dto.TeacherSubjectsDTO;
import com.timetable.generator.entity.Teacher;
import com.timetable.generator.service.HtmlService;
import com.timetable.generator.service.TimeTableService;

@RestController
@RequestMapping("/timetable")
public class TimeTableController {
    
     @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private HtmlService htmlService;

    @GetMapping("/subject/{subjectName}")
    public List<Teacher> getTeachersHandlingSubject(@PathVariable String subjectName) {
        return timeTableService.getTeachersHandlingSubject(subjectName);
    }
    @GetMapping("/getsubject")
    public List<TeacherSubjectsDTO> getSubject() {
        return timeTableService.getuniqueSubject();
    }

    @GetMapping("/gettimetable/{numberOfClasses}")
    public String getTimetable(@PathVariable int numberOfClasses) {
        // List<List<List<TeacherSubjectsDTO>>> timetable = timeTableService.generateTimetables(numberOfClasses);
        return htmlService.getContent(timeTableService.generatorNew(numberOfClasses));
    }

    @GetMapping("/timetable/{numberOfClasses}")
    public List<List<List<TeacherSubjectsDTO>>> gettimetable(@PathVariable int numberOfClasses) {

        // return timeTableService.generateTimetables(numberOfClasses);
        return timeTableService.generatorNew(numberOfClasses);
    }

}
