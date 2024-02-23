package waitandnotify.quiz;

import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CountdownLatch latch = new CountdownLatch(2);

        Thread task1 = new Task1(latch);
        Thread task2 = new Task2(latch);

        task1.start();
        task2.start();

        latch.await();
        System.out.println("all task completed ");
    }
    private static class Task1 extends Thread{

        private CountdownLatch latch;
        public Task1(CountdownLatch latch){
            this.latch =latch;
        }
        @Override
        public void run(){
            try {
                Thread.sleep(2000);
                System.out.println("task1 is completed");
                latch.countdown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static class Task2 extends Thread{

        private CountdownLatch latch;

        public Task2(CountdownLatch latch){
            this.latch= latch;
        }
        @Override
        public void run(){
            try {
                Thread.sleep(2000);
                System.out.println("task2 is completed");
                latch.countdown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class CountdownLatch {
        private int count;

        public CountdownLatch(int count){
            if (count<0)
                throw new RuntimeException("error occurred");

            this.count= count;
        }

        public synchronized void await() throws InterruptedException {

            while (count > 0)
                wait();

        }

        public void countdown() {

            synchronized (this) {
                count--;

                if (count == 0)
                    notifyAll();
            }
        }

    }
}
