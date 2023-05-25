package ua.foxminded.university.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private String groupId;
    private String groupName;
    private int count;
    
    public Group(String groupId, String groupName) {
	this.groupId = groupId;
	this.groupName = groupName;
    }
}
