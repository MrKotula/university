package ua.foxminded.university.tools;

public enum Status {
    NEW("New"), STUDENT("Student"), TEACHER("Teacher");

    private String strStatus;

    Status(String strStatus) {
	this.strStatus = strStatus;
    }

    public String getStatus() {
	return strStatus.toUpperCase();
    }
}
