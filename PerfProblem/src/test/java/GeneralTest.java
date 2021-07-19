import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GeneralTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static ArrayList<Integer> primeNumbers = new ArrayList<>();
    private final String maxIntPlusOneString = String.valueOf(Integer.MAX_VALUE + 1);

    @BeforeEach
    public void setPrinter() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void removePrinter() {
        System.setOut(null);
    }

    @BeforeAll
    static void readExpectedPrimeNumbers() throws IOException, CsvException {
        String fileName = "src/test/resources/primeNumbers.csv";
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(fileName))
                .build()) {
            List<String[]> r = reader.readAll();
            r.forEach(line -> {
                primeNumbers.add(Integer.parseInt(line[0]));
            });
        }
    }

    private String getExpectedPrimeNumbers(int maxPrimeNumber) {
        String resultString = "";
        for (Integer primeNumber: primeNumbers) {
            if (primeNumber <= maxPrimeNumber) {
                resultString = resultString + (primeNumber.toString() + "\n");
            } else break;
        }
        return resultString;
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 100, 101, 10000})
    public void positiveTest(Integer inputValue) throws InterruptedException, IOException {
        String[] args = {inputValue.toString()};
        PerfProblem.main(args);
        assertEquals(getExpectedPrimeNumbers(inputValue), outContent.toString(), "Wrong prime numbers list");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null", "-1", "1", "maxIntPlusOneString", "test string", "2.7"})
    public void negativeTest(String inputValue) throws InterruptedException, IOException {
        String[] args = {inputValue};
        PerfProblem.main(args);
        assertEquals("", outContent.toString(), "Prime numbers list not empty");
    }


}
