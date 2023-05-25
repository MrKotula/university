package ua.foxminded.university.viewprovider;

public interface ViewProvider {
    void printMessage(String message);
    
    String read();
    
    int readInt();
    
    boolean readBoolean();
}
