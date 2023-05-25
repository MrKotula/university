package ua.foxminded.university.viewprovider;

public interface View {
    void printMessage(String message);
    
    String read();
    
    int readInt();
    
    boolean readBoolean();
}
