package socket.io;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class Writer extends Thread {

    private final LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        boolean busyLooping = true;
        while (busyLooping) {
            try {
                Node node = queue.take();
                node.out.writeObject(node.string);
            } catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted via InterruptedIOException");
                busyLooping = false;
            } catch (IOException | InterruptedException e) {
                if (!isInterrupted()) {
                    e.printStackTrace();
                } else {
                    System.out.println("Interrupted");
                }
                busyLooping = false;
            }
        }
    }

    public void write(String str, ObjectOutputStream out) {
        queue.add(new Node(str, out));
    }

    private static class Node {
        public final String string;
        public final ObjectOutputStream out;

        public Node(String string, ObjectOutputStream out) {
            this.string = string;
            this.out = out;
        }
    }

}
