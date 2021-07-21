import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.function.Consumer;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.fail;

public class PerformanceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private double getCalibrationVelocityForCPU() {
        double inputNumber = 2334345;
        final int retries = 1000000;
        long resultTime = 0;

        for (int i = 0; i < retries; i++) {
            long startTime = System.currentTimeMillis();
            if (calculateSqrt(inputNumber) > 0) {
                resultTime += System.currentTimeMillis() - startTime;
            }
        }

        return ((double) retries) / resultTime;
    }

    private static double calculateSqrt(double inputNumber) {
        final double EPS = 0.000001;
        double x = 1;
        while (true) {
            double nx = (x + inputNumber / x) / 2;
            if (abs(x - nx) < EPS) return x;
            else x = nx;
        }
    }

    private double meanSqrtVelocity;

    @BeforeEach
    public void calculateActualMeanVelocity() {
        // get the runtime object associated with the current Java application
        Runtime runtime = Runtime.getRuntime();

        // get the number of processors available to the Java virtual machine
        int numberOfProcessors = runtime.availableProcessors();

        //noinspection OptionalGetWithoutIsPresent
        meanSqrtVelocity = Arrays.stream(new int[numberOfProcessors])
                .parallel()
                .mapToDouble(operand -> getCalibrationVelocityForCPU())
                .average()
                .getAsDouble();

        System.out.println("Actual mean sqrt velocity = " + meanSqrtVelocity + " ops/ms");
    }

    private void performanceTest(Consumer<String[]> mainMethod) {
        final long referenceTime = 50;
        final double referenceSqrtVelocity = 6500; //mean sqrt velocity which got on my local machine
        final String[] args = {"10000"};

        //warm up
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(outContent));
        for (int i = 0; i < 10; i++) {
            mainMethod.accept(args);
        }

        boolean success = false;
        long minActualTime = Long.MAX_VALUE;

        double adjustedTime = (referenceSqrtVelocity / meanSqrtVelocity) * referenceTime;

        for (int i = 0; i < 3; i++) {
            long startTime = System.currentTimeMillis();

            mainMethod.accept(args);

            long endTime = System.currentTimeMillis();

            minActualTime = Math.min((endTime - startTime), minActualTime);

            if (minActualTime <= adjustedTime) {
                success = true;
                break;
            }
        }
        System.setOut(stdout);
        System.out.println("Actual time:  " + minActualTime + "ms");
        if (!success)
            fail("Actual time for calculating list of prime numbers more than expected. Actual time: " + minActualTime + " ms" +
                    "\nExpected time: " + adjustedTime + " ms");
    }

    @Test
    public void performanceTestForOriginalProgram() {
        performanceTest(PerfProblem::main);
    }

    @Test
    public void performanceTestForFixedProgram() {
        performanceTest(PerfProblemAfterFixes::main);
    }
}
