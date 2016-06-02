package org.esgi.pooler;

/**
 * Created by matpe on 01/06/2016.
 */
public interface IPooledQueue<E> {

    public void enqueue(E e);

    public  E dequeue();
}
