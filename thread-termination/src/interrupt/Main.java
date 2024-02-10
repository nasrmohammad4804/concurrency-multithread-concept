package interrupt;

import java.math.BigInteger;

import static java.math.BigInteger.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Thread thread = new BlockingTask();
        thread.setName("blocking-task-thread");

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (e instanceof RuntimeException)
                    System.out.println("interrupt exception occurred while running : "+t.getName());
            }
        });

        thread.start();
        thread.interrupt();


    }
    private static class BlockingTask extends Thread{

        @Override
        public void run() {
            try {
                Thread.sleep(20000);
                System.out.println("new thread executed successfully");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
class LongCalculationTest{
    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new LongCalculation(200,40000));

        thread.start();

        /*
        * consider we can wait for long calculation task only 3 second . and if don't finished we raise interrupt signal
        * that we want to terminate that . but because  don't exist any method may interrupt exception (wait - join - sleep)
        * we need to handle that explicitly on that block .raise interrupt signal to owner of thread . and we consider business logic
        * at owner of thread at each iteration of raised interrupt signal we stop that calculation
        * */
        Thread.sleep(3000);
        thread.interrupt();
    }
    private static class LongCalculation implements Runnable{

        private final int base;
        private final int pow;

        private BigInteger result;

        public LongCalculation(int base, int pow) {
            this.base = base;
            this.pow = pow;
            result =ONE;
        }

        @Override
        public void run() {
            result = calculate();
            System.out.printf("%d^%d : %d",base,pow,result);
        }

        /*we handle termination explicitly . by considering part of may took time with check of isInterrupted and go out from that */
        public BigInteger calculate(){
            for (int start = 1 ; start<=pow ; start++){
                if (Thread.currentThread().isInterrupted())
                    return ZERO;

                result = result.multiply(valueOf(base));

            }
            return  result;
        }

        public int getBase() {
            return base;
        }

        public int getPow() {
            return pow;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}