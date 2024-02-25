package atomic;

public interface CustomStack<E extends Comparable<E>> extends Countable {

    void push(E element);

    E pop();

    int size();
}
