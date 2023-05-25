package ua.foxminded.university.viewprovider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ViewProviderTest {

    @Mock
    private Scanner scanner;

    @InjectMocks
    private ViewProviderImpl viewProviderImpl;

    @Test
    void printMessageShouldReturnMessageFromConsole() {
	ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	System.setOut(new PrintStream(outputStreamCaptor));

	viewProviderImpl.printMessage("word");

	assertThat("word").isEqualTo(outputStreamCaptor.toString().trim());
    }

    @Test
    void readIntShouldReturnIntegerFromScannerMocked() {
	int expected = 5;
	when(scanner.nextInt()).thenReturn(5);

	viewProviderImpl.printMessage("5");
	int actual = viewProviderImpl.readInt();

	assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readStringShouldReturnNextLineFromScannerMocked() {
	String input = "hello";
	when(scanner.next()).thenReturn("hello");

	viewProviderImpl.printMessage(input);
	input = viewProviderImpl.read();

	assertThat("hello").isEqualTo(input);
    }

    @Test
    void readStringShouldReturnHasNextFromScannerMocked() {
	when(scanner.hasNext()).thenReturn(true);
	boolean input = true;
	input = viewProviderImpl.readBoolean();

	assertTrue(input);
    }
}
