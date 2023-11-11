package com.timetable.generator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.generator.dto.TeacherSubjectsDTO;
import com.timetable.generator.entity.Teacher;
import com.timetable.generator.repository.TeacherRepository;

@Service
public class TimeTableService {

    @Autowired
    private TeacherRepository teacherRepository;

    List<TeacherSubjectsDTO> handleSubjects = new ArrayList<>();

    public List<Teacher> getTeachersHandlingSubject(String subjectName) {
        return teacherRepository.findBySubjectName(subjectName);
    }

    public List<TeacherSubjectsDTO> getuniqueSubject() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        List<String> noOfSubjects = new ArrayList<>();
        List<TeacherSubjectsDTO> handleSubjects = new ArrayList<>();
        for (Teacher teacher : allTeachers) {
            String subjects[] = teacher.getSubjects().split(",");
            for (String subject : subjects) {
                handleSubjects.add(new TeacherSubjectsDTO(teacher.getTeacherName(), subject));
                if (!noOfSubjects.contains(subject))
                    noOfSubjects.add(subject);
            }
        }
        return handleSubjects;
    }

    public List<List<List<TeacherSubjectsDTO>>> generateTimetables(int numberOfClasses) {
        List<TeacherSubjectsDTO> teachers = getuniqueSubject();

        int days = 6; // Number of days
        int periods = 8; // Number of periods per day

        List<List<List<TeacherSubjectsDTO>>> timetables = new ArrayList<>();

        for (int i = 0; i < numberOfClasses; i++) {
            List<List<TeacherSubjectsDTO>> timetable = new ArrayList<>();

            for (int day = 0; day < days; day++) {
                List<TeacherSubjectsDTO> shuffledTeachers = new ArrayList<>(teachers);
                Collections.shuffle(shuffledTeachers);

                List<TeacherSubjectsDTO> dailySchedule = new ArrayList<>();

                for (int period = 0; period < periods; period++) {
                    TeacherSubjectsDTO teacher = findAvailableTeacher(shuffledTeachers, dailySchedule, i, day);

                    if (teacher != null) {
                        dailySchedule.add(teacher);
                    } else {
                        dailySchedule.add(new TeacherSubjectsDTO()); // Placeholder for no teacher
                    }
                }

                timetable.add(dailySchedule);
            }

            timetables.add(timetable);
        }

        return timetables;
    }

    private static TeacherSubjectsDTO findAvailableTeacher(List<TeacherSubjectsDTO> teachers,
            List<TeacherSubjectsDTO> dailySchedule, int currentClassIndex, int currentDay) {
        for (TeacherSubjectsDTO teacher : teachers) {
            // Check if the teacher is not already assigned in the current period for
            // another class on the same day
            boolean isAlreadyAssigned = dailySchedule.stream()
                    .anyMatch(assignedTeacher -> assignedTeacher != null &&
                            assignedTeacher.getTeacherName().equals(teacher.getTeacherName()) &&
                            assignedTeacher.getSubjects().equals(teacher.getSubjects()) &&
                            dailySchedule.indexOf(assignedTeacher) != dailySchedule.lastIndexOf(assignedTeacher));

            if (!isAlreadyAssigned) {
                // Check if the teacher is not already assigned in the current period for the
                // current class on the same day
                if (!dailySchedule.contains(teacher) &&
                        !dailySchedule.stream()
                                .filter(assignedTeacher -> assignedTeacher != null)
                                .anyMatch(assignedTeacher -> assignedTeacher.getTeacherName()
                                        .equals(teacher.getTeacherName()) &&
                                        assignedTeacher.getSubjects().equals(teacher.getSubjects()) &&
                                        dailySchedule.indexOf(assignedTeacher) != dailySchedule
                                                .lastIndexOf(assignedTeacher))) {
                    return teacher;
                }
            }
        }

        return null; // Return null if no available teacher is found
    }

}
