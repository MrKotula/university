package ua.foxminded.university.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(of= {"userId", "firstName", "lastName", "email"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    protected String userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    @ToString.Exclude protected String password;
    
    public User(String firstName, String lastName, String email, String password) {
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
	this.password = password;
    }
    
    public User(String firstName, String lastName) {
	this.firstName = firstName;
	this.lastName = lastName;
    }
}
