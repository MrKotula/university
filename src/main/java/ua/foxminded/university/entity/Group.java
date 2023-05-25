package ua.foxminded.university.entity;

import java.util.Objects;

public class Group {
    private String groupId;
    private String groupName;
    private int count;

    public Group() {
	
    }
    
    public Group(String groupId, String groupName) {
	this.groupId = groupId;
	this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getCount() {
        return count;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int hashCode() {	
	return Objects.hash(groupId, groupName, count);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Group other = (Group) obj;
	
	return 	Objects.equals(groupName, other.groupName) &&
		Objects.equals(count, other.count) &&
		Objects.equals(groupId, other.groupId); 
    }

    @Override
    public String toString() {
	return "Group [groupId= " + groupId + '\'' + "," + " groupName=" + groupName + ", count= " + count + "]";
    }
}
