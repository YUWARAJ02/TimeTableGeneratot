package com.timetable.generator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.generator.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    @Query("SELECT t FROM Teacher t WHERE t.subjects LIKE %:subjectName%")
    List<Teacher> findBySubjectName(String subjectName);
}
