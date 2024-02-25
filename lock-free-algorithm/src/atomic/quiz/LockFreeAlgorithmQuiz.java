package atomic.quiz;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class LockFreeAlgorithmQuiz {

    /*
     * if we had multiple thread that first get average every 1 minute
     * and second add sample each second we had race condition
     * we must use lock or synchronization or lock free solution such as atomic with compareAndSet method
     * */

    private final AtomicLong count = new AtomicLong(0);
    private final AtomicLong sum = new AtomicLong(0);

    public void addSample(long sample) {


        while (true) {

            long previousCount = count.get();
            long previousSum = sum.get();

            long newCount = previousCount + 1;
            long newSum = previousSum + sample;

            if (count.compareAndSet(previousCount, newCount) && sum.compareAndSet(previousSum, newSum))
                break;

            else LockSupport.parkNanos(10);

        }

    }

    public double getAverage() {

        double average;
        AtomicLong currentCount;
        AtomicLong currentSum;

        do {
            currentCount = count;
            currentSum = sum;

            average = (double) currentSum.get() / currentCount.get();

        } while (count.compareAndSet(currentCount.get(), 0) && sum.compareAndSet(currentSum.get(), 0));
        return average;
    }

}
