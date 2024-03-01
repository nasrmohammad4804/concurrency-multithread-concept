import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {

    private static final int THREAD_NUMBER = 1000;

    public static void main(String[] args) throws InterruptedException {


        Runnable runnable = () -> System.out.println("inside thread : " + Thread.currentThread() + " is daemon : " + Thread.currentThread().isDaemon());


//        Thread platformThread = new Thread(runnable);

       /* Thread platformThread = Thread.ofPlatform().unstarted(runnable);
        platformThread.start();
        platformThread.join();*/



        /*
         * it uses thread per core with nonblocking approach give optimal performance
         * but if we create thread we size of core for example 8 (platform thread) every thread had blocking io operation
         * such as network request . database connection . access to file system .it blocks current thread(platform thread)
         * and other task can't use that thread until release that.
         * but if we use virtual thread we 8 number of platform thread of one of virtual thread need io operation it doesn't thread
         * it store instruction pointer and stack data of specific virtual thread into heap and unmount that and mount another virtual thread
         * this point because we achieve thread per core with optimal performance . and use required resource
         * it leads always platform thread available and achieve parallelism as much as possible
         * */

//        calculation with platform with 8 thread as number of cpu core and each task take 1 second
//        in best scenario every second finished 8 operation and 1000 task take 125 second around it but with context switch between that it take 126 second
//        calculateCompletionPlatformThread();


        /* calculation with virtual thread with number of platform thread as well as cpu core(8) each virtual task when
         * mounted on platform thread while need to operate io operation it unmounted (instruction pointer and stack save to heap)
         *  and another virtual thread able to mount to that platform thread and after sometime io operation is finished previous
         * task can be mounted to platform thread again and continue that
         * it give us optimal performance with nonblocking platform thread
         *
         */
//        calculationCompletionVirtualThread();
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    private static void calculateCompletionPlatformThread() throws InterruptedException {

        long startTime = System.currentTimeMillis();

        Runnable blockingTask = new BlockingTask();

        try (ExecutorService threadPool = Executors.newFixedThreadPool(8)) {

            IntStream.rangeClosed(1, THREAD_NUMBER)
                    .forEach(index -> {
                        threadPool.submit(blockingTask);
                    });
        }


        System.out.println("all task completed as platform after : " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static void calculationCompletionVirtualThread() throws InterruptedException {

        Runnable blockingTask = new BlockingTask();

        List<Thread> threads = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        IntStream.rangeClosed(1, THREAD_NUMBER)
                .forEach(index -> {
                    Thread thread = Thread.ofVirtual().unstarted(blockingTask);
                    threads.add(thread);
                });

        for (int index = 0; index < THREAD_NUMBER; index++)
            threads.get(index).start();

        for (Thread thread : threads)
            thread.join();


        System.out.println("all task completed as virtual after : " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("before blocking call at thread : " + Thread.currentThread());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task finished at thread : " + Thread.currentThread());
        }
    }
}