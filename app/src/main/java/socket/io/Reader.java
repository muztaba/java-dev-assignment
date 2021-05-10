package socket.io;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;

public class Reader extends Thread {

    private final ObjectInputStream objectInputStream;

    public Reader(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        boolean busyLooping = true;
        while (busyLooping) {

            try {
                System.out.println((String) objectInputStream.readObject());
            } catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted via InterruptedIOException");
                busyLooping = false;
            } catch (IOException | ClassNotFoundException e) {
                if (!isInterrupted()) {
                    e.printStackTrace();
                } else {
                    System.out.println("Interrupted");
                }
                busyLooping = false;
            }

        }
    }

    public void interrupt() {
        super.interrupt();
        try {
            objectInputStream.close();
        } catch (IOException e) {
        } // quietly close
    }
}
