package socket.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class Writer implements Runnable {

    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final ObjectOutputStream objectOutputStream;

    public Writer(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String str = queue.take();
                objectOutputStream.writeObject(str);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void write(String str) {
        queue.add(str);
    }

    public void interrupt() {
        Thread.interrupted();
    }
}
