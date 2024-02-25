package atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

class StackTest {
    public static void main(String[] args) throws InterruptedException {

        /*
         * read and write to head variable is our critical section
         * */

        int numberOfThreadForPush = 2;
        int numberOfThreadForPop = 2;

        CustomStack<Integer> stack=  new LockFreeStackImpl<>();

        calculateNumberOfOperationPerformedWithSpecificStack(numberOfThreadForPush, numberOfThreadForPop,stack);
        Thread.sleep(10000);
        System.out.println("number of operation executed : "+stack.getCount());
    }

    private static  void calculateNumberOfOperationPerformedWithSpecificStack(int numberOfThreadForPush, int numberOfThreadForPop,CustomStack<Integer> stack) {


        Random random = new Random();
        final int MAX_NUMBER = 10;

        List<Thread> threads = new ArrayList<>();

        for (int index = 1; index <= numberOfThreadForPush; index++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt(MAX_NUMBER));
                }
            });

            thread.setDaemon(true);
            threads.add(thread);
        }

        for (int index = 1; index <= numberOfThreadForPop; index++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }
        for (Thread thread : threads)
            thread.start();

    }



    private static class StackNode<E extends Comparable<E>> {
        private final E value;
        private StackNode<E> next;

        private StackNode<E> previous;

        public StackNode(E value) {
            this.value = value;
        }

        public StackNode(E value, StackNode<E> next) {
            this.value = value;
            this.next = next;
        }

        public StackNode<E> getPrevious() {
            return previous;
        }

        public E getValue() {
            return value;
        }

        public StackNode<E> getNext() {
            return next;
        }
    }

    private static class ConcurrentStackImpl<E extends Comparable<E>> implements CustomStack<E>, Countable {


        private StackNode<E> head;
        private int count = 0;

        @Override
        public synchronized void push(E element) {

            if (head == null) {
                count++;
                head = new StackNode<>(element);
                return;
            }
            StackNode<E> node = new StackNode<>(element);

            count++;
            head.previous = node;
            node.next = head;
            head = node;
        }


        @Override
        public synchronized E pop() {
            if (head == null) {
                count++;
                return null;
            }

            count++;
            E element = head.value;
            StackNode<E> node = head.next;

            if (node == null)
                head = null;
            else {
                node.previous = head;
                head = node;
            }


            return element;
        }

        public synchronized int size() {

            count++;
            if (head == null)
                return 0;

            int counter = 0;
            StackNode<E> node = head;
            while (node.next != null) {
                counter++;
                node = node.next;
            }
            return counter;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    /*
    * we use compareAndSet for lock free algorithm
    * for check between duration of read data and time we want to update any other thread change that
    * if don't changed it means it atomic and we can update it otherwise it need to process later
    * we use while because may occur few push or pop concurrent and we need few attempt to succeed.
    * */
    private static class LockFreeStackImpl<E extends Comparable<E>> implements CustomStack<E> {

        private final AtomicReference<StackNode<E>> head = new AtomicReference<>();

        private final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public void push(E element) {

            StackNode<E> node = new StackNode<>(element);

            while (true) {
                StackNode<E> currentHeadNode = head.get();
                node.next = currentHeadNode;
                if (head.compareAndSet(currentHeadNode, node))
                    break;

                else LockSupport.parkNanos(1);
            }
            counter.incrementAndGet();
        }

        @Override
        public E pop() {

            E element = null;

            while (true) {

                StackNode<E> currentHead = head.get();

                StackNode<E> nextNode = null;

                if (currentHead != null) {
                    element = currentHead.value;
                    nextNode = currentHead.next;
                }

                if (head.compareAndSet(currentHead, nextNode))
                    break;

                else LockSupport.parkNanos(1);
            }
            counter.incrementAndGet();
            return element;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public int getCount() {
            return counter.get();
        }
    }
}




