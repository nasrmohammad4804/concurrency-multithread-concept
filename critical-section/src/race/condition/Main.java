package race.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Metric metric= new Metric();

        BusinessLogic businessLogic1 = new BusinessLogic(metric);
        BusinessLogic businessLogic2 = new BusinessLogic(metric);

        MetricPrinter metricPrinter = new MetricPrinter(metric);

        businessLogic1.start();
        businessLogic2.start();

        metricPrinter.start();

    }
    public static class MetricPrinter extends Thread{

        private final Metric metric;

        public MetricPrinter(Metric metric) {
            this.metric = metric;
        }

        @Override
        public void run(){
            while (true){

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                double currentAverage = metric.getAverage();
                System.out.println("current average is : "+currentAverage);
            }
        }
    }
    public static class BusinessLogic extends Thread{

        private final Metric metric;
        private final Random random = new Random();

        public BusinessLogic(Metric metric) {
            this.metric = metric;

        }
        @Override
        public void run(){

            while (true){
                long startTime = System.currentTimeMillis();

                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long endTime = System.currentTimeMillis();
                metric.addSample(endTime - startTime);
            }

        }
    }

    public static class Metric {
        private long count = 0;
        private volatile double average = 0;

        public void addSample(long sample) {
            double currentSum = average * count;
            synchronized (this) {
                count++;
                average = (currentSum + sample) / count;

            }


        }

        public  double getAverage() {
            return this.average;
        }

        public long getCount() {
            return this.count;
        }
    }
}
