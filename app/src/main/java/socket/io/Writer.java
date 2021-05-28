package socket.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class Writer extends Thread {

    private final LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Node node = queue.take();
                node.out.writeObject(node.string);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Shutting down writer thread...");
                return;
            } catch (IOException e) {
                e.printStackTrace();
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
