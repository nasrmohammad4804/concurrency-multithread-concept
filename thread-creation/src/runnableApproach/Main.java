package runnableApproach;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger("Main.java");
    public static void main(String[] args) throws InterruptedException {

        Thread thread= new Thread(() -> {
//            run that will in new thread
            System.out.println("we are now in thread : "+Thread.currentThread().getName());
            System.out.println("current thread priority is : "+Thread.currentThread().getPriority());

            throw new RuntimeException("business error occurred");
        });

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.warning(String.format("error occurred in thread : %s with message : %s",t.getName(),e.getMessage()));
            }
        });
        thread.setName("new-thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        System.out.println("we are in thread : "+Thread.currentThread().getName()+" before starting new thread");
        thread.start();
        System.out.println("we are in thread : "+Thread.currentThread().getName()+" after starting new thread");

//        sleep method cause to operating system change state of runnable state to blocked and don't associate any cpu until time reach
        Thread.sleep(1000);

    }
}
