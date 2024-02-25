package atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();

        Thread producer = new ProducerThread(inventory);
        Thread consumer = new ConsumerThread(inventory);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(inventory.getData());
    }

    private static class ConsumerThread extends Thread{

        private  final Inventory inventory;

        public ConsumerThread(Inventory inventory){
            this.inventory =  inventory;
        }
        @Override
        public void run(){
            for (int index =1 ; index<10000; index++)
                inventory.decrement();
        }
    }
    private static class ProducerThread extends Thread{

        private  final Inventory inventory;

        public ProducerThread(Inventory inventory){
            this.inventory =  inventory;
        }
        @Override
        public void run(){
            for (int index =1 ; index<10000; index++)
                inventory.increment();
        }
    }
    private static class Inventory{
        private final AtomicInteger atomicInteger = new AtomicInteger(0);

        public void increment(){
            atomicInteger.incrementAndGet();
        }
        public void decrement(){
            atomicInteger.decrementAndGet();
        }
        public int getData(){
            return atomicInteger.get();
        }
    }
}

