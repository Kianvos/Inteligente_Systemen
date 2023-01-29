package Helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CsvLogger {
    private final Path csvOutputFile;
    private final CSVFormat.Builder builder;
    private CSVFormat csvFormat;


    public CsvLogger(String filePath) {
        this(new File(filePath).toPath());
    }

    public CsvLogger(Path filePath) {
        this.csvOutputFile = filePath;

        builder = CSVFormat.Builder.create();
        builder.setHeader(BoardStateCSVHeader.class);
        builder.setDelimiter(',');
        builder.setRecordSeparator(System.lineSeparator());
        this.csvFormat = builder.build();
    }

    public void writeDataToCsv(int transpositionTableSize, double averageTime, double standardDeviation) {
        if (csvOutputFile.toFile().exists()) {
            this.csvFormat = builder.setSkipHeaderRecord(true).build();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                csvOutputFile,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)
        ) {
            CSVPrinter printer = new CSVPrinter(writer, csvFormat);

            printer.printRecord(transpositionTableSize, averageTime, standardDeviation);

            printer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private enum BoardStateCSVHeader {
        TRANSPOSITION_TABLE_SIZE, AVERAGE_TIME_PER_MOVE, STANDARD_DEVIATION
    }
}
