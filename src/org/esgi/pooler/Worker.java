package org.esgi.pooler;

/**
 * Created by matpe on 01/06/2016.
 */
public class Worker implements Runnable {

    private PooledQueue<Runnable> queue;

    public Worker(PooledQueue<Runnable> threadQueue) {
        queue = threadQueue;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("Taken item by thread " + Thread.currentThread().getId());
            Runnable r = queue.dequeue();
            r.run();
            System.out.println("Task completed of thread" + Thread.currentThread().getId());
        }
    }
}
