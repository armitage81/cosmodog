package antonafanasjew.cosmodog.model.portals;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class FixedSizeQueue<T> extends AbstractQueue<T> implements Serializable {

    private final ArrayBlockingQueue<T>  queue;
    private final int capacity;

    public FixedSizeQueue(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.capacity = capacity;
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean offer(T e) {
        if (capacity == queue.size()) {
            this.poll();
        }
        this.queue.offer(e);
        return true;
    }

    @Override
    public T poll() {
        return (T) queue.poll();
    }

    @Override
    public T peek() {
        return queue.peek();
    }

    public static void main(String[] args) {
        Queue<String> queue = new FixedSizeQueue<String>(2);
        System.out.println("Expecting 0: Actual: " + queue.size());
        queue.offer("A");
        System.out.println("Expecting 1: Actual: " + queue.size());
        System.out.println("Expecting A: Actual: " + queue.peek());
        queue.offer("B");
        System.out.println("Expecting 2: Actual: " + queue.size());
        System.out.println("Expecting A: Actual: " + queue.peek());
        queue.offer("C");
        System.out.println("Expecting 2: Actual: " + queue.size());
        System.out.println("Expecting B: Actual: " + queue.peek());


    }

}
