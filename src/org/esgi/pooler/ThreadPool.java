package org.esgi.pooler;

/**
 * Created by matpe on 01/06/2016.
 */
public class ThreadPool {

    private final int threadPoolSize;
    private PooledQueue<Runnable> threadQueue;

    public ThreadPool(int poolSize) {
        this.threadPoolSize = poolSize;
        this.threadQueue = new PooledQueue<>();
        initConsumers();
    }

    private void initConsumers(){
        for (int i = 0; i < threadPoolSize; i++){
            Thread thread = new Thread(new Worker(threadQueue));
            thread.start();
        }
    }

    public void submitTask(Runnable runnable){
        threadQueue.enqueue(runnable);
    }

}
