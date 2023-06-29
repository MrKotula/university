package ua.foxminded.university.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(of= {"userId", "firstName", "lastName", "email"})
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    protected String userId;
    
    @Column(name = "first_name")
    protected String firstName;
    
    @Column(name = "last_name")
    protected String lastName;
    
    @Column(name = "email")
    protected String email;
    
    @Column(name = "password")
    @ToString.Exclude 
    protected String password;
    
    protected User(String firstName, String lastName, String email, String password) {
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
	this.password = password;
    }
    
    protected User(String firstName, String lastName) {
	this.firstName = firstName;
	this.lastName = lastName;
    }
}
