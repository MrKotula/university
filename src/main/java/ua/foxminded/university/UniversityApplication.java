package ua.foxminded.university;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.foxminded.university.menu.MenuController;

@SpringBootApplication
public class UniversityApplication implements CommandLineRunner {
    @Autowired
    MenuController menuController;

    public static void main(String[] args) {
	SpringApplication.run(UniversityApplication.class, args);
    }
    
    public void run(final String... s) {
	menuController.startMenu();
    }
}
