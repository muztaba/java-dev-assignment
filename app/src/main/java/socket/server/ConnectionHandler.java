package socket.server;

import socket.io.Writer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ConnectionHandler implements Runnable {

    private static final String EXIT = "EXIT";

    private final Socket socket;
    private final ExecutorService workerPool;
    private Writer writer;

    private ConnectionHandler(Socket socket, ExecutorService workerPool) {
        this.socket = socket;
        this.workerPool = workerPool;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
        ) {
            startWriterThreadAndInitThreadPool(objectOutputStream);
            while (true) {
                String requestAsString = (String) objectInputStream.readObject();

                if (EXIT.equalsIgnoreCase(requestAsString)) {
                    System.out.println("Closing client connection...");
                    return;
                }

                System.out.println("Message from client: " + requestAsString);
                workerPool.submit(Worker.newWorker(requestAsString, writer));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeSocket();
        }
    }

    private void startWriterThreadAndInitThreadPool(ObjectOutputStream objectOutputStream) {
        writer = new Writer(objectOutputStream);
        writer.start();
    }

    private void closeSocket() {
        try {
            writer.interrupt();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionHandler of(Socket socket, ExecutorService workerPool) {
        return new ConnectionHandler(socket, workerPool);
    }

}
