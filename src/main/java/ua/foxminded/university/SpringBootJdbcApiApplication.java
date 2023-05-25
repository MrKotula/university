package ua.foxminded.university;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.foxminded.university.menu.MenuStarter;

@SpringBootApplication
public class SpringBootJdbcApiApplication implements CommandLineRunner {
    @Autowired
    MenuStarter menuStarter;

    public static void main(String[] args) {
	SpringApplication.run(SpringBootJdbcApiApplication.class, args);
    }
    
    public void run(final String... s) {
	menuStarter.startMenu();
    }
}
