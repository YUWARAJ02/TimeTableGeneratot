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
                // Collections.shuffle(shuffledTeachers);

                List<TeacherSubjectsDTO> dailySchedule = new ArrayList<>();

                for (int period = 0; period < periods; period++) {
                    TeacherSubjectsDTO teacher = findclasses(shuffledTeachers, dailySchedule, i, day,
                            timetables);

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

    public static TeacherSubjectsDTO findclasses(List<TeacherSubjectsDTO> teachers,
            List<TeacherSubjectsDTO> dailySchedule, int currentClassIndex, int currentDay,
            List<List<List<TeacherSubjectsDTO>>> timetables) {

        if (timetables.size() != 0) {
            for (int i = 0; i < currentClassIndex; i++) {
                List<List<TeacherSubjectsDTO>> classes = timetables.get(i);
                // for (int day = 0; day <= currentDay; day++) {
                    List<TeacherSubjectsDTO> weekdays = classes.get(currentDay);
                    if(dailySchedule.size()==0) {
                        for (int j = 0; j < teachers.size(); j++) {
                           if (!teachers.get(j).equals(weekdays.get(0))) {
                             return teachers.get(j);
                           } 
                        } 

                    }else {
                    for (int period=0; period<dailySchedule.size();period++) {
                        if(!dailySchedule.get(period).equals(weekdays.get(period))) {
                            for(int j=0;j<teachers.size();j++) {
                                if(!dailySchedule.contains(teachers.get(j))
                                    && !teachers.get(j).equals(weekdays.get(period))) {
                                    return teachers.get(j);
                                }
                            }
                        }
                        else {
                            for(int j=0;j<teachers.size();j++) {
                                if(!dailySchedule.contains(teachers.get(j))
                                && !teachers.get(j).equals(weekdays.get(period))) {
                                    return teachers.get(j);
                                }
                            }
                        }
                        

                    }
                }
                // }

            }
        }
        else 
        return findAvailableTeacher(teachers,
             dailySchedule,  currentClassIndex,  currentDay,
             timetables);
        
        return null;
    }

    private static TeacherSubjectsDTO findAvailableTeacher(List<TeacherSubjectsDTO> teachers,
            List<TeacherSubjectsDTO> dailySchedule, int currentClassIndex, int currentDay,
            List<List<List<TeacherSubjectsDTO>>> timetables) {
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
