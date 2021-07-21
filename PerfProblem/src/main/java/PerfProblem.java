import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BigIntegerIterator {
    private final List<String> contain = new ArrayList<>(500); //зачем на одно значение массив из 500?
    private final List<Integer> reference = new ArrayList<>(500);

    BigIntegerIterator(int i) {
        contain.add("" + i + "");
        reference.add(i);
    }

    Integer getContain() {
        return Math.max(Integer.decode(contain.get(0)),reference.get(0));
    }
}

public class PerfProblem {
    public static void main(String[] args) {
        try {
            for (Integer prime : getPrimes(Integer.parseInt(args[0]))) {
                System.out.print(prime + "\n");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Integer> getPrimes(int maxPrime) throws InterruptedException {
        List<Integer> primeNumbers = Collections.synchronizedList(new LinkedList<>()); // линкед лист лучше заменить эррей листом
        List<BigIntegerIterator> myFiller = Stream.generate(new Supplier<BigIntegerIterator>() {
            int i = 2;

            @Override
            public BigIntegerIterator get() {
                return new BigIntegerIterator(i++);
            }
        }).limit(maxPrime).collect(Collectors.toList());

        for (BigIntegerIterator integer : myFiller) {
            primeNumbers.add(integer.getContain());
        }

        List<Integer> primeNumbersToRemove = Collections.synchronizedList(new LinkedList<>());
        CountDownLatch latch = new CountDownLatch(maxPrime);
        ExecutorService executors = Executors.newFixedThreadPool(Math.max(maxPrime / 100, 3000)); //столько потоков (3000) слишком, зачем брать макс? нужно выделять столько потоков сколько есть CPU
        synchronized (primeNumbersToRemove) {
            for (Integer candidate : primeNumbers) {
                executors.submit(() -> { //переписать лямбды
                    try {
                        isPrime(primeNumbers, candidate); //primeNumbers не участвует в параллельной работе, только чтение, можно использровать immutable лист
                    } catch (Exception e) {
                        primeNumbersToRemove.add(candidate); //нельзя вот так через эксепшены, это долго
                    }
                    latch.countDown();
                });
            }
        }
        latch.await();
        executors.shutdownNow();
        for (Integer toRemove : primeNumbersToRemove) {
            primeNumbers.remove(toRemove);
        }
        return primeNumbers;
    }

    private static void isPrime(List<Integer> primeNumbers, Integer candidate) throws Exception {
        for (Integer j : primeNumbers.subList(0, candidate - 2)) { // не нужно выделять саблист, достаточно работы по индексам
            if (candidate % j == 0) {
                throw new Exception();
            }
        }
    }
}
