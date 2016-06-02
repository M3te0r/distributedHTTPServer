package org.esgi.pooler;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by matpe on 01/06/2016.
 */
public class PooledQueue<E> implements IPooledQueue<E> {

    private LinkedBlockingQueue<E> blockingQueue;


    @Override
    public synchronized void enqueue(E e) {
        blockingQueue.add(e);
        notify();
    }

    @Override
    public synchronized E dequeue() {
        E e = null;
        while (blockingQueue.isEmpty()){
            try {
                wait();

            }catch (InterruptedException exv){
                return e;
            }
        }
        e = blockingQueue.remove();
        return e;
    }

    public PooledQueue() {
        blockingQueue = new LinkedBlockingQueue<>();
    }
}
