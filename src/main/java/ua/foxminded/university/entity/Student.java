package ua.foxminded.university.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.foxminded.university.enums.Status;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)
@Entity
@Table(name="students", schema = "schedule")
public class Student extends User {
    
    @Column(name = "group_id")
    private String groupId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    
    public Student(String groupId, String firstName, String lastName, Status status) {
	super(firstName, lastName);
	this.groupId = groupId;
	this.status = status;
    }
    
    public Student(String groupId, String firstName, String lastName, String email, String password, Status status) {
	super(firstName, lastName, email, password);
	this.groupId = groupId;
	this.status = status;
    }
    
    public Student(String userId, String groupId, String firstName, String lastName, String email, String password, Status status) {
	super(userId, firstName, lastName, email, password);
	this.groupId = groupId;
	this.status = status;
    }
    
    public Student(String firstName, String lastName, String email, String password, Status status) {
	super(firstName, lastName, email, password);
	this.status = status;
    }
}
