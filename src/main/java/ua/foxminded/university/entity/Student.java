package ua.foxminded.university.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.foxminded.university.tools.Status;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class Student extends User {
    private String studentId;
    private String groupId;
    private Status status;
    
    public Student(String groupId, String firstName, String lastName, Status status) {
	super(firstName, lastName);
	this.groupId = groupId;
	this.status = status;
    }
    
    public Student(String studentId, String groupId, String firstName, String lastName, String email, String password, Status status) {
	super(firstName, lastName, email, password);
	this.studentId = studentId;
	this.groupId = groupId;
	this.status = status;
    }
    
    public Student(String firstName, String lastName, String email, String password, Status status) {
	super(firstName, lastName, email, password);
	this.status = status;
    }
    
    @Override
    public String toString() {
	return "Student [studentId=" + studentId + '\'' +", groupId=" + groupId + '\'' + ", firstName=" + firstName + '\'' + 
		", lastName=" + lastName + '\'' + ", email=" + email + '\'' + ", status=" + status + "]";
    }
}
