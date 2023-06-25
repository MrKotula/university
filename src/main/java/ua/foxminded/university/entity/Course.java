package ua.foxminded.university.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="courses", schema = "schedule")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="course_id")
    private String courseId;
    
    @Column(name="course_name")
    private String courseName;
    
    @Column(name="course_description")
    private String courseDescription;

    public Course(String courseName, String courseDescription) {
	this.courseName = courseName;
	this.courseDescription = courseDescription;
    }
}
