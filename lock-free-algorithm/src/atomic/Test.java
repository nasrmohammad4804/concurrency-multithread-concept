package atomic;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Test {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        /*provide useful method that execute atomic and integer value*/

        System.out.println(atomicInteger.incrementAndGet()); // return new value
        System.out.println(atomicInteger.getAndIncrement()); // return previous value

        System.out.println(atomicInteger.decrementAndGet()); // decrement and return new value
        System.out.println(atomicInteger.getAndDecrement()); // return previous value and after decrement that

        /* advantage :
            simplicity
            no need to for lock or synchronization
            no race condition or data race

            cons:
            only the operation itself is atomic
            there still race condition between 2 separate atomic operation
        *
        * */

        // atomic integer is great tool for concurrent counting or aggregating of metric without complexity of using lock


        String oldName = "oldName";
        String newName = "newName";

        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);

        if (atomicReference.compareAndSet(oldName,newName))
            System.out.println("new value is : "+atomicReference.get());

        else System.out.println("nothing changed");


    }
}
