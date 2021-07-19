import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParStream {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3);

        // we use default common ForkJoinPool
        List<Integer> collect = list.parallelStream()
                .filter(ParStream::isPrime)
                .collect(Collectors.toList());
    }

    private static boolean isPrime(Integer integer) {
        if (integer == 0) {
            return true;
        } else {
            return false;
        }
    }
}
