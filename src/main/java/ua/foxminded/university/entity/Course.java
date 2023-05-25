package ua.foxminded.university.entity;

import java.util.Objects;

public class Course {
    private String courseId;
    private String courseName;
    private String courseDescription;
    
    public Course() {
	
    }
    
    public Course(String courseId, String courseName, String courseDescription) {
	this.courseId = courseId;
	this.courseName = courseName;
	this.courseDescription = courseDescription;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }  

    @Override
    public int hashCode() {
	return Objects.hash(courseDescription, courseId, courseName);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Course other = (Course) obj;
	
	return 	Objects.equals(courseDescription, other.courseDescription) &&
		Objects.equals(courseName, other.courseName);
    }

    @Override
    public String toString() {
	return "Course [courseName=" + courseName + '\'' + ", courseDescription=" + courseDescription + '\'' + ", courseId= " + courseId + "]";
    }
}
