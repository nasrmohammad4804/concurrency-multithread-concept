package threadsubclass;

public class Main {

    private static final int PASSWORD_MAX_NUMBER= 9999;
    public static void main(String[] args) {

        Vault vault = new Vault(2400);

        Thread[] threads = {
                new AscendingHacker(vault),
                new DescendingHacker(vault),
                new Police()
        };

        for (Thread thread : threads)
                thread.start();
    }
    private static class Vault{
        private final int password;

        public Vault(int password) {
            this.password = password;
        }
        public boolean isCorrect(int guessPassword) throws InterruptedException {
//            take 3 second for checking
            Thread.sleep(3);
            return password==guessPassword;
        }


    }
    private abstract static class Hacker extends Thread{

        protected Vault vault;

        public Hacker(Vault vault) {
            this.vault = vault;
            setName(this.getClass().getSimpleName());
        }

        @Override
        public void start() {
            System.out.println("thread : "+this.getName()+" started !");
            super.start();
        }
    }
    private static class AscendingHacker extends Hacker{

        public AscendingHacker(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int count = 0; count<=PASSWORD_MAX_NUMBER; count++) {
                try {
                    if (vault.isCorrect(count)){
                        System.out.println("thread : "+this.getName()+" find password : "+count);
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class DescendingHacker extends Hacker{

        public DescendingHacker(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int count = PASSWORD_MAX_NUMBER; count>=0; count--) {
                try {
                    if (vault.isCorrect(count)){
                        System.out.println("thread : "+this.getName()+" find password : "+count);
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private static class Police extends Thread{

        @Override
        public void run() {
            for (int count=10; count>=1; count--){
                try {
                    Thread.sleep(1000);
                    System.out.println(count);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println("He arrested the thieves . and police won ");
            System.exit(0);
        }
    }

}
