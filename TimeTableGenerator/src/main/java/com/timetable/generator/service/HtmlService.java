package com.timetable.generator.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Component;

import com.timetable.generator.dto.TeacherSubjectsDTO;

@Component
public class HtmlService {

    public String getContent(List<List<List<TeacherSubjectsDTO>>> timetable) {
        String daysList[] ={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head><style>");
        sb.append(style());
        sb.append("</style></head><body><br/>");
        for (int classIndex = 0; classIndex < timetable.size(); classIndex++) {
            

            sb.append("<table>"
            +"<thead>"
            +"    <tr>"
            +"        <th>Day/Period</th>"
            +"        <th>Period 1</th>"
            +"        <th>Period 2</th>"
            +"        <th>Period 3</th>"
            +"        <th>Period 4</th>"
            +"        <th>Period 5</th>"
            +"        <th>Period 6</th>"
            +"        <th>Period 7</th>"
            +"        <th>Period 8</th>"
            +"    </tr>"
            +"</thead>");
            sb.append("<h2>Class ").append(classIndex + 1).append(" Timetable</h2>");
            List<List<TeacherSubjectsDTO>> classTimetable = timetable.get(classIndex);

            for (int day = 0; day < classTimetable.size(); day++) {
                sb.append("<tbody><tr>");
                sb.append("<td>").append(daysList[day]).append("</td>");

                List<TeacherSubjectsDTO> dailySchedule = classTimetable.get(day);

        //         sb.append("<ul>");

                for (int period = 0; period < dailySchedule.size(); period++) {
                    TeacherSubjectsDTO teacher = dailySchedule.get(period);

                    // sb.append("<li><strong>Period ").append(period + 1).append(":</strong> ");
                    sb.append("<td class=\""+teacher.getSubjects()+"\">");
                    if (teacher != null && !teacher.getTeacherName().isEmpty()) {
                        sb.append(teacher.getTeacherName()).append(" - ").append(teacher.getSubjects());
                    } else {
                        sb.append("No teacher assigned");
                    }
                    sb.append("</td>");

        //             sb.append("</li>");
                }

        //         sb.append("</ul>");
            }

            sb.append("<br>");
        }

        sb.append("</body></html>");

        return sb.toString();
    }

    private String style() {
        StringBuilder sb = new StringBuilder();
        InputStream ins = HtmlService.class.getClassLoader().getResourceAsStream("static/style.css");
        InputStreamReader inr = new InputStreamReader(ins,StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inr);
        if(reader!=null)
            reader.lines().forEach(sb::append);
        
        return sb.toString();
    }
}
