import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int TASK_NUMBER = 10000;

    public static void main(String[] args) {


        long startTime = System.currentTimeMillis();

        calculateIoOperation();
        System.out.println("takes : " + (System.currentTimeMillis() - startTime));
    }

    private static void calculateIoOperation() {

        try (ExecutorService threadpool = Executors.newFixedThreadPool(30000)) {
            for (int index = 1; index < TASK_NUMBER; index++)
                threadpool.execute(Main::ioOperation);
        } catch (Exception e) {

        }


    }

    private static void ioOperation() {

        try {
            System.out.println("io operation is received at thread : " + Thread.currentThread().getName());
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }

    }
}