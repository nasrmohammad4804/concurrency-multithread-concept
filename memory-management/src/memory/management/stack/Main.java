package memory.management.stack;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    private int testData = 3;
    private final LocalDate myDate = LocalDate.now();
    public static void main(String[] args) {

        /*
        simulating stack data and heap data
        * stack main ##
        args( as local variable )
        x =1
        y =2
        result (with value after calculated)
        ##

        stack sum ##
        local variable a (with value 1)
        local variable b (with value 2)
        local variable s (with value of 3)
        reference list (but object created as arrayList goes to heap ##

        heap ##
        class member -> (testData with value 3) (myDate with value of now) (array list object create at sum stack)
        ##
        */
        int x= 1;
        int y= 2;
        int result = sum(x,y);
        System.out.println(result);
    }

    private static int sum(int a, int b) {
        int s = a + b;
        List<Integer> list = new ArrayList<>();
        return s;
    }
}
