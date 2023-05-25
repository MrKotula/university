package ua.foxminded.university.viewprovider;

import java.util.Scanner;
import org.springframework.stereotype.Service;

@Service
public class ViewProvider implements View {
    private Scanner scanner;
    
    public ViewProvider() {
	scanner = new Scanner(System.in);
    }

    public void printMessage(String message) {
	System.out.println(message);
    }

    public String read() {
	return scanner.next();
    }

    public int readInt() {
	return scanner.nextInt();
    }
    
    public boolean readBoolean() {
	return scanner.hasNext();
    }
}
