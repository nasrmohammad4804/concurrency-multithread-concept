import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Long> numbers= List.of(94241761362675L,345L,2324L,4656L,23L,2435L);
        List<Thread> threads = new ArrayList<>();

        for (long number : numbers){
            Thread thread = new FactorialCalculation(number);
            threads.add(thread);
            thread.start();
        }

        /*
        we at first join with time of 2 second if task don't complete then release that and we want to raise interrupt
        * we use interrupt method but maybe cant schedule for that thread to execute to have chance to go to interrupt handler block
        and finish that execution
        if we use only join(2000) and interrupt and my number is so much after calculation on number in 2 second
        we raise interrupt signal but if don't control to thread and cant handle signal then this operation is useless
        for that we are using join to confidence operation done completely
        */
        for (Thread thread : threads){
            thread.join(2000);
//            we wait maximum 2 second for each thread to propagate it after that current thread dont wait for that
//            also we need to if after 2 second don't finished terminated implicitly because may take long time and utilize resource
            thread.interrupt();
            thread.join();
        }

        for (Thread thread : threads){
            FactorialCalculation calculation = (FactorialCalculation) thread;
            if (calculation.getIsFinished()){
                System.out.println(thread.getName()+" successfully finished with factorial : "+calculation.getNumber()+" is : "+ calculation.getResult());
                continue;
            }
            System.out.println(thread.getName()+" is upon running with factorial of : "+calculation.getNumber()+" with value : "+calculation.getResult());

        }



    }
    private static class FactorialCalculation extends Thread{
        private final long  number;
        private BigInteger result;

        private volatile boolean isFinished;

        public FactorialCalculation(long number) {
            this.number = number;
            result=BigInteger.ONE;
            isFinished=false;
        }

        @Override
        public void run() {
           calculate();
            isFinished=true;
        }

        public void calculate(){
            for (long index=number; index>=1; index--){
                if (this.isInterrupted()){
                    result = BigInteger.ZERO;
                    System.out.println("interrupted");
                    break;
                }
                result=result.multiply(BigInteger.valueOf(index));
            }
        }
        public BigInteger getResult(){
            return result;
        }
        public boolean getIsFinished(){
            return isFinished;
        }

        public long getNumber() {
            return number;
        }
    }



}