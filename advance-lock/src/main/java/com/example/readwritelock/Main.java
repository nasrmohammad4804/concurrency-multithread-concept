package com.example.readwritelock;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class Main {
    private static final int HIGHEST_PRICE = 1000;
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {

        InventoryDatabase inventoryDatabase = new InventoryDatabase();

        for (int index = 1; index < 100000; index++)
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));

        Thread writer = getWriterThread(inventoryDatabase);

        long startTime = System.currentTimeMillis();
//        writer.start();

        final int numberOfReaders = 7;
        List<Thread> threads = new ArrayList<>();

        for (int index = 1; index <= numberOfReaders; index++) {
            Thread thread = new Thread(() -> {

                for (int start = 1; start < 100000; start++) {

                    int upperBound = random.nextInt(HIGHEST_PRICE);
                    int lowerBound = upperBound > 0 ? random.nextInt(upperBound) : 0;

                    inventoryDatabase.getNumberOfItemInPriceOfRange(lowerBound, upperBound);

                }

            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for (Thread thread : threads)
            thread.start();

        for (Thread thread : threads)
            thread.join();

        long endTime = System.currentTimeMillis();
        System.out.println("total execution take : " + (endTime - startTime));
    }

    private static Thread getWriterThread(InventoryDatabase inventoryDatabase) {
        Thread writer = new Thread(() -> {

            while (true) {
                inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        writer.setDaemon(true);
        return writer;
    }

    public static class InventoryDatabase {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private final Lock myLock = new ReentrantLock();
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();

        public void removeItem(int price) {

            lock.writeLock().lock();
//            myLock.lock();
            try {

                Integer value = priceToCountMap.get(price);

                if (value == null || value == 1)
                    priceToCountMap.remove(price);

                else {
                    priceToCountMap.put(price, --value);
                }
            } finally {
                lock.writeLock().unlock();
//                myLock.unlock();
            }
        }

        public int getNumberOfItemInPriceOfRange(int lowerBound, int upperBound) {

            lock.readLock().lock();
//            myLock.lock();
            try {

                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null)
                    return 0;

                NavigableMap<Integer, Integer> subMap = priceToCountMap.subMap(fromKey, true, toKey, true);

                return subMap.values().stream()
                        .reduce(0, Integer::sum);
            } finally {
                lock.readLock().unlock();
//                myLock.unlock();
            }
        }

        public void addItem(int price) {

            lock.writeLock().lock();
//            myLock.lock();
            try {

                Integer previousCount = priceToCountMap.putIfAbsent(price, 1);

                if (Optional.ofNullable(previousCount).isPresent())
                    priceToCountMap.put(price, ++previousCount);
            } finally {
                lock.writeLock().unlock();
//                myLock.unlock();
            }
        }
    }

}
