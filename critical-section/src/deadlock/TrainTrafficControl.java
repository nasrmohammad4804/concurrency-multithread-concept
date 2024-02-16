package deadlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainTrafficControl {
    public static void main(String[] args) {

        /*
        * in this scenario may occur deadlock because trainA get roadA at same time trainB get roadB
        * and after time trainA want to get roadB because finished roadA and want to go to roadB
        * but before that roadB takes by trainB and trainA suspended also after some time trainA finishes roadB and want
        * to go to roadA but roadA already taken by trainA and both train stuck
        *
        * to solve this issue we need to allow 1 train goes to road at one time means until train finish path through roadA and roadB
        * another train can not be goes to road
        * */
        Intersection intersection = new Intersection();

        Thread[] threads = {
                new Thread(new TrainA(intersection)),
                new Thread(new TrainB(intersection))
        };

        for (Thread thread : threads)
            thread.start();

    }

    public static class TrainB implements Runnable {
        private final Intersection intersection;

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {

            /* 5 ms wait until train comes */


            try {
                Thread.sleep(5);
                intersection.takeRoadB();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class TrainA implements Runnable {

        private final Intersection intersection;

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {

            /* 5ms  wait until train comes */

            try {
                Thread.sleep(5);
                intersection.takeRoadA();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Intersection {
        private final Object roadA = new Object();
        private final Object roadB = new Object();

        public void takeRoadA() throws InterruptedException {

            synchronized (roadA) {

                System.out.println("road A is acquired by : " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("train is passing through road A");
                    Thread.sleep(1);
                }

            }
        }

        public void takeRoadB() throws InterruptedException {

            synchronized (roadB) {

                System.out.println("road B is acquired by : " + Thread.currentThread().getName());
                synchronized (roadA) {
                    System.out.println("train is passing through road B");
                    Thread.sleep(1);
                }

            }

        }
    }
}
