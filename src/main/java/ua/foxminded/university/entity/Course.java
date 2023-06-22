package ua.foxminded.university.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    private String courseId;
    private String courseName;
    private String courseDescription;

    public Course(String courseName, String courseDescription) {
	this.courseName = courseName;
	this.courseDescription = courseDescription;
    }
}
