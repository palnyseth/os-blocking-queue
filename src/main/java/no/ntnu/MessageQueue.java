package no.ntnu;

import java.util.ArrayList;

public class MessageQueue implements Channel {

    private final ArrayList<Object> queue;
    private final int size;

    public MessageQueue(int size) {
        if (size < 1) {
            size = 1; // Do not allow weird size
        }
        
        this.size = size;
        queue = new ArrayList<>(size);
    }

    @Override
    public synchronized void send(Object item) {
        // TODO - block if the queue is full
        try {
            while (this.queue.size() == this.size) {
                System.out.println("Queue full, blocking...");
                wait();
            }
        } catch (InterruptedException e) {
            //Exception
        } finally {
            queue.add(item);
            notify();
        }
    }


    // implements a nonblocking receive
    @Override
    public synchronized Object receive() {
        // TODO - block if the queue is empty, and always return the first
        try {
            while (queue.isEmpty()) {
                wait();
            }
        } catch (InterruptedException e) {
            //Exception
        } finally {
            notify();
            return queue.remove(0);
        }
    }

    @Override
    public synchronized int getNumQueuedItems() {
        return queue.size();
    }

    /**
     * Return comma-separated objects
     * @return 
     */
    @Override
    public synchronized String getQueueItemList() {
        String res = "";
        for (Object item : queue) {
            res += item + ",";
        }
        return res;
    }
}