package daemon;

public class Main {
    public static void main(String[] args) {

        Thread thread=  new newThread();
        thread.start();
        System.out.println("end block of "+Thread.currentThread().getName());
    }
    private static class newThread extends Thread{

        public newThread() {
            this.setDaemon(true);
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                System.out.println("daemon thread finished successfully");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
