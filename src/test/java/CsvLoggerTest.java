import Helper.CsvLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvLoggerTest {
    CsvLogger csvLogger;
    Path temp;

    @BeforeEach
    public void createBoardStateSerializer() {
        try {
            this.temp = Files.createTempFile("test-average-times", ".csv");
            this.temp.toFile().deleteOnExit();
            this.csvLogger = new CsvLogger(this.temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCanWriteToFile() throws IOException {
        this.csvLogger.writeDataToCsv(10, 100.0, 5, 0, 0, 0, 0, 0);

        long fileSize = Files.size(temp);

        assertTrue(fileSize > 0);
    }
}
