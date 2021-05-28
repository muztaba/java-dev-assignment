package socket.io;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.util.function.Consumer;

public class Reader<T> extends Thread {

    private final ObjectInputStream objectInputStream;
    private final Consumer<T> consumer;

    public Reader(ObjectInputStream objectInputStream, Consumer<T> consumer) {
        this.objectInputStream = objectInputStream;
        this.consumer = consumer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        boolean busyLooping = true;
        while (busyLooping) {
            try {
                T response = (T) objectInputStream.readObject();
                consumer.accept(response);
            } catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted via InterruptedIOException");
                busyLooping = false;
            } catch (IOException | ClassNotFoundException e) {
                if (isInterrupted()) {
                    System.out.println("Interrupted");
                } else {
                    e.printStackTrace();
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

    public static <T> ReaderBuilder<T> builder(){
        return new ReaderBuilder<>();
    }

    public static class ReaderBuilder<T> {
        private ObjectInputStream objectInputStream;
        private Consumer<T> consumer;

        public ReaderBuilder<T> setObjectInputStream(ObjectInputStream objectInputStream) {
            this.objectInputStream = objectInputStream;
            return this;
        }

        public ReaderBuilder<T> setConsumer(Consumer<T> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Reader<T> createReader() {
            return new Reader<>(objectInputStream, consumer);
        }
    }
}
