package socket.io;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class Writer extends Thread {

    private final LinkedBlockingQueue<String> queue;
    private final ObjectOutputStream objectOutputStream;

    public Writer(ObjectOutputStream objectOutputStream) {
        this.queue = new LinkedBlockingQueue<>();
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        boolean busyLooping = true;
        while (busyLooping) {
            try {
                String str = queue.take();
                objectOutputStream.writeObject(str);
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

    public void write(String str) {
        queue.add(str);
    }

    public void interrupt() {
        super.interrupt();
        try {
            objectOutputStream.close();
        } catch (IOException e) {
        } // quietly close
    }

}
