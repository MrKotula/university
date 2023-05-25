package ua.foxminded.university.tools;

import java.util.UUID;
import org.springframework.stereotype.Component;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class IdProvider {

    public String generateUUID() {
	return UUID.randomUUID().toString();
    }
}
