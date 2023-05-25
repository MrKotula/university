package ua.foxminded.university.entity;

import java.util.Objects;

public class Student {
    private String studentId;
    private String groupId;
    private String firstName;
    private String lastName;

    public Student() {

    }
    
    public Student(String groupId, String firstName, String lastName) {
	this.groupId = groupId;
	this.firstName = firstName;
	this.lastName = lastName;
    }

    public Student(String studentId, String groupId, String firstName, String lastName) {
	this.studentId = studentId;
	this.groupId = groupId;
	this.firstName = firstName;
	this.lastName = lastName;
    }

    public void setGroupId(String groupId) {
	this.groupId = groupId;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public void setStudentId(String studentId) {
	this.studentId = studentId;
    }

    public String getStudentId() {
	return studentId;
    }

    public String getGroupId() {
	return groupId;
    }

    public String getFirstName() {
	return firstName;
    }

    public String getLastName() {
	return lastName;
    }

    @Override
    public int hashCode() {
	return Objects.hash(firstName, groupId, lastName, studentId);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Student other = (Student) obj;

	return 	Objects.equals(firstName, other.firstName) && 
		Objects.equals(groupId, other.groupId) && 
		Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
	return "Student [studentId=" + studentId + "', groupId=" + groupId + '\'' + ", firstName=" + firstName
		+ '\'' + ", lastName=" + lastName + "]";
    }
}
