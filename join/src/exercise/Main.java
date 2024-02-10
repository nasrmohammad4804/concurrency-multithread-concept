package exercise;

import java.math.BigInteger;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        /*
        ( 2 ^ 3 + 4 ^ 3) = 72
        */
        calculate(
                BigInteger.valueOf(2)
                , BigInteger.valueOf(3)
                , BigInteger.valueOf(4)
                , BigInteger.valueOf(3)
        );
    }

    public static void calculate(BigInteger base1, BigInteger pow1, BigInteger base2, BigInteger pow2) throws InterruptedException {

        BigInteger result = BigInteger.ZERO;

        List<Thread> threads = List.of(
                new PowerCalculationThread(base1, pow1),
                new PowerCalculationThread(base2, pow2)
        );

        for (Thread thread : threads)
            thread.start();

        for (Thread thread : threads) {
            thread.join();
            result = ((PowerCalculationThread) thread).getResult().add(result);
        }


        System.out.printf("result of (%d ^ %d + %d ^ %d) = %d", base1, pow1, base2, pow2, result);
    }
}

class PowerCalculationThread extends Thread {

    private final BigInteger base;
    private final BigInteger pow;

    private BigInteger result;

    public PowerCalculationThread(BigInteger base, BigInteger pow) {
        this.base = base;
        this.pow = pow;
        result = BigInteger.ONE;
    }

    @Override
    public void run() {
        for (BigInteger start = BigInteger.ZERO; start.compareTo(pow) != 0; start = start.add(BigInteger.ONE))
            result = result.multiply(base);


    }

    public BigInteger getResult() {
        return result;
    }
}