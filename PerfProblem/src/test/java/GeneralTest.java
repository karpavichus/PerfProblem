import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GeneralTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ArrayList<Integer> primeNumbers = new ArrayList<>();
    private PrintStream defaultStdout;

    @BeforeEach
    public void setPrinter() {
        defaultStdout = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void removePrinter() {
        System.setOut(defaultStdout);
    }

    @BeforeAll
    static void readExpectedPrimeNumbers() throws IOException, CsvException {
        try (InputStream stream = GeneralTest.class.getResourceAsStream("/primeNumbers.csv");
             CSVReader reader = new CSVReaderBuilder(new InputStreamReader(stream)).build()) {

            List<String[]> r = reader.readAll();
            r.forEach(line -> primeNumbers.add(Integer.parseInt(line[0])));
        }
    }

    private String getExpectedPrimeNumbers(int maxPrimeNumber) {
        StringBuilder resultString = new StringBuilder();
        for (Integer primeNumber : primeNumbers) {
            if (primeNumber <= maxPrimeNumber) {
                resultString.append(primeNumber.toString());
                resultString.append("\n");
            } else break;
        }
        return resultString.toString();
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 100, 101, 10000})
    public void positiveTest(Integer inputValue) throws InterruptedException, IOException {
        String[] args = {inputValue.toString()};
        PerfProblem.main(args);
        String actual = new String(outContent.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(getExpectedPrimeNumbers(inputValue), actual, "Wrong prime numbers list");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null", "-1", "1", "test string", "2.7"})
    public void negativeTest(String inputValue) throws InterruptedException, IOException {
        String[] args = {inputValue};
        PerfProblem.main(args);
        String actual = new String(outContent.toByteArray(), StandardCharsets.UTF_8);
        assertEquals("", actual, "Prime numbers list not empty");
    }


}
