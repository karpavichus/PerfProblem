import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BigIntegerIteratorAfterFix {
    private final Integer contain;

    BigIntegerIteratorAfterFix(int i) {
        contain = i;
    }

    Integer getContain() {
        return contain;
    }
}

public class PerfProblemAfterFixes {
    public static void main(String[] args) {
        for (Integer prime : getPrimes(Integer.parseInt(args[0]))) {
            System.out.print(prime + "\n");
        }
    }

    private static List<Integer> getPrimes(int maxPrime) {
        List<Integer> primeNumbers = new ArrayList<>();
        List<BigIntegerIteratorAfterFix> myFiller = Stream.generate(new Supplier<BigIntegerIteratorAfterFix>() {
            int i = 2;

            @Override
            public BigIntegerIteratorAfterFix get() {
                return new BigIntegerIteratorAfterFix(i++);
            }
        }).limit(maxPrime-1).collect(Collectors.toList());

        for (BigIntegerIteratorAfterFix integer : myFiller) {
            primeNumbers.add(integer.getContain());
        }

        // we use default common ForkJoinPool
        return primeNumbers.parallelStream()
                .filter(i -> isPrime(primeNumbers, i))
                .collect(Collectors.toList());
    }

    private static boolean isPrime(List<Integer> primeNumbers, Integer candidate) {
        for (Integer j : primeNumbers.subList(0, candidate - 2)) {
            if (candidate % j == 0) {
                return false;
            }
        }
        return true;
    }
}

