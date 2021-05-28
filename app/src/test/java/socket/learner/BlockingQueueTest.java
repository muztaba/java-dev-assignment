package socket.learner;


import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueTest {

    @Test
    public void testBlockingQueueInterrupted() {
        Node node = new Node();
        node.start();
        node.interrupt();
    }

    public static class Node extends Thread {

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

        @Override
        public void run() {
            try {
                queue.take();
                System.out.println("try block isInterrupted" + Thread.currentThread().isInterrupted());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                System.out.println("catch block isInterrupted " + Thread.currentThread().isInterrupted());
            }
        }

    }

}
