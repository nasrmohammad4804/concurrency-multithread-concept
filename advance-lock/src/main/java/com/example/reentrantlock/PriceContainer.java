package com.example.reentrantlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PriceContainer {

    private Lock lock = new ReentrantLock();

    private double bitcoinPrice;
    private double etherPrice;
    private double litecoinPrice;
    private double bitcoinCashPrice;
    private double ripplePrice;

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public double getBitcoinPrice() {
        return bitcoinPrice;
    }

    public void setBitcoinPrice(double bitcoinPrice) {
        this.bitcoinPrice = bitcoinPrice;
    }

    public double getEtherPrice() {
        return etherPrice;
    }

    public void setEtherPrice(double etherPrice) {
        this.etherPrice = etherPrice;
    }

    public double getLitecoinPrice() {
        return litecoinPrice;
    }

    public void setLitecoinPrice(double litecoinPrice) {
        this.litecoinPrice = litecoinPrice;
    }

    public double getBitcoinCashPrice() {
        return bitcoinCashPrice;
    }

    public void setBitcoinCashPrice(double bitcoinCashPrice) {
        this.bitcoinCashPrice = bitcoinCashPrice;
    }

    public double getRipplePrice() {
        return ripplePrice;
    }

    public void setRipplePrice(double ripplePrice) {
        this.ripplePrice = ripplePrice;
    }
}
