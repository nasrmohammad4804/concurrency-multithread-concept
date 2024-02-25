package atomic.quiz;

public class Metric {
    private long count;
    private long sum;

    public void addSample(int sample) {
        sum += sample;
        count++;
    }

    public double getAverage() {
        double average = (double) sum / count;
        reset();
        return average;
    }

    public void reset() {
        this.count = 0;
        this.sum = 0;
    }
}
