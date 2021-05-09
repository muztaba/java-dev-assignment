package socket.io;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Reader implements Runnable {

    private final ObjectInputStream objectInputStream;

    public Reader(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println((String) objectInputStream.readObject());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public void interrupt() {
        Thread.interrupted();
    }
}
