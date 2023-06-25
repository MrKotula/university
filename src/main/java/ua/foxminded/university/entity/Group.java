package ua.foxminded.university.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="groups", schema = "schedule")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    private String groupId;
    
    @Column(name = "group_name")
    private String groupName;
    
    @Transient
    private int count;
    
    public Group(String groupId, String groupName) {
	this.groupId = groupId;
	this.groupName = groupName;
    }
    
    public Group(String groupName) {
	this.groupName = groupName;
    }
}
