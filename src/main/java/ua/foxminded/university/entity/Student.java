package ua.foxminded.university.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of= {"groupId", "firstName", "lastName"})
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String studentId;
    private String groupId;
    private String firstName;
    private String lastName;
    
    public Student(String groupId, String firstName, String lastName) {
	this.groupId = groupId;
	this.firstName = firstName;
	this.lastName = lastName;
    }
}
