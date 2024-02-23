package semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Quiz {

    /*
     * our problem is intercommunication between thread we want to sure all thread complete part1 and after that
     * threads goes to part2
     * */
    public static void main(String[] args) {

        final int numberOfThreads = 3;
        List<Thread> threads = new ArrayList<>();
        Semaphore semaphore = new Semaphore(0);
        Barrier barrier = new Barrier(new ReentrantLock(), semaphore, numberOfThreads);

        for (int index = 1; index <= numberOfThreads; index++)
            threads.add(
                    new MyTask(barrier)
            );


        for (Thread thread : threads)
            thread.start();
    }

    public static class MyTask extends Thread {

        private Barrier barrier;

        public MyTask(Barrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()
                    + " part 1 of the work is finished");

            try {
                barrier.waitForAnother();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName()
                    + " part 2 of the work is finished");
        }
    }

    public static class Barrier {

        private Semaphore semaphore;
        private Lock lock;
        private int count;
        private long start = 0;

        public Barrier(Lock lock, Semaphore semaphore, int count) {
            this.lock = lock;
            this.semaphore = semaphore;
            this.count = count;
        }

        public void waitForAnother() throws InterruptedException {

            lock.lock();

            start++;
            if (start < count) {
                lock.unlock();
                semaphore.acquire();

            } else {

                lock.unlock();

                int end = 1;
                while (end < count) {
                    semaphore.release();
                    end++;
                }

            }

        }
    }
}
