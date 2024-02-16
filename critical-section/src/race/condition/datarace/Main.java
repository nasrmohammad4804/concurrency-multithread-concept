package race.condition.datarace;

public class Main {

    public static void main(String[] args) {
        SharedClass sharedClass=  new SharedClass();

        Thread incrementor = new Thread(() -> {
            for (int index= 1 ; index<Integer.MAX_VALUE ; index++)
                sharedClass.increment();
        });

        Thread checkDataRace = new Thread(() -> {
            for (int index = 1; index<Integer.MAX_VALUE; index++)
                    sharedClass.checkForDataRace();
        });

        incrementor.start();
        checkDataRace.start();
    }

    public static class SharedClass{
        private volatile int x = 0;
        private volatile int y = 0;

        public  void increment(){
            x++;
            y++;
        }
        public  void checkForDataRace(){
            if (y>x){
                System.out.println("y > x data race is detected");
            }
        }
    }
}
