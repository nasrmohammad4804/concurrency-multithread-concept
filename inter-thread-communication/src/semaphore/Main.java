package semaphore;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {

        Queue queue = new ArrayDeque();
        Semaphore full = new Semaphore(0);
        Semaphore empty = new Semaphore(10);
        Lock lock = new ReentrantLock();

        Thread producer = new Producer(full, empty, lock, queue);
        Thread consumer = new Consumer(full, empty, lock, queue);

        producer.start();
        consumer.start();
    }

    public static class Producer extends Thread {
        private final Semaphore full;
        private final Semaphore empty;
        private final Lock lock;
        private final Queue queue;

        private final Random random = new Random();

        public Producer(Semaphore full, Semaphore empty, Lock lock, Queue queue) {

            this.full = full;
            this.empty = empty;
            this.lock = lock;
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    empty.acquire();
                    int random = this.random.nextInt(1000);
                    lock.lock();
                    queue.add(random);
                    lock.unlock();
                    full.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Semaphore full;
        private final Semaphore empty;
        private final Lock lock;
        private final Queue queue;

        public Consumer(Semaphore full, Semaphore empty, Lock lock, Queue queue) {

            this.full = full;
            this.empty = empty;
            this.lock = lock;
            this.queue = queue;
        }

        @Override
        public void run() {

            Object data;
            while (true) {

                try {
                    full.acquire();
                    lock.lock();
                    data = queue.poll();
                    System.out.println("data released is : " + data);
                    lock.unlock();

                    empty.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
