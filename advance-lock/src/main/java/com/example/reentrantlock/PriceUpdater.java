package com.example.reentrantlock;

import java.util.Random;

public class PriceUpdater extends Thread {

    private final PriceContainer priceContainer;
    private final Random random;

    public PriceUpdater(PriceContainer priceContainer) {
        this.priceContainer = priceContainer;
        random = new Random();

    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                update();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {

        priceContainer.getLock().lock();
        try {
            Thread.sleep(4000);
            priceContainer.setEtherPrice(random.nextInt(1400));
            priceContainer.setBitcoinPrice(random.nextInt(2000));
            priceContainer.setBitcoinCashPrice(random.nextInt(3500));
            priceContainer.setLitecoinPrice(random.nextInt(1800));
            priceContainer.setRipplePrice(random.nextInt(4100));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            priceContainer.getLock().unlock();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

    }
}
