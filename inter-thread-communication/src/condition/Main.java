package condition;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static String data = null;

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        /*
         * we want to implement scenario inter thread communication with lock and condition as alternative solution instead of wait and notify
         * a thread wait for user input received and process it and wait until data received from console and another thread give data from console and signal that
         *
         * */

        Thread processor  = new DataProcessor(lock,condition);

        Thread receiver = new DataReceiver(lock,condition);

        processor.start();
        receiver.start();

    }

    private static class DataReceiver extends Thread {

        private final Lock lock;
        private final Condition condition;
        private final Scanner scanner = new Scanner(System.in);

        public DataReceiver(Lock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
            this.setName(getClass().getSimpleName());
        }

        @Override
        public void run() {

            lock.lock();
            try {
                System.out.println("enter your name : ");
                data = scanner.nextLine();
                condition.signal();

            } finally {
                lock.unlock();
            }
        }
    }

    private static class DataProcessor extends Thread {

        private final Lock lock;
        private final Condition condition;

        public DataProcessor(Lock lock, Condition condition) {

            this.lock = lock;
            this.condition = condition;
            this.setName(getClass().getSimpleName());
        }

        @Override
        public void run() {

            lock.lock();
            try {


                while (data == null)
                    condition.await();

                System.out.printf("name received for processing in thread is : %s with value : %s %n",this.getName(),data);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }

        }
    }
}
