package socket.server;

import socket.io.Writer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionHandler implements Runnable {

    private static final String EXIT = "EXIT";

    private final Socket socket;

    private ConnectionHandler(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
        ) {
            ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            Writer writer = new Writer(objectOutputStream);
            new Thread(writer).start();
            while (true) {
                String requestAsString = (String) objectInputStream.readObject();

                if (EXIT.equalsIgnoreCase(requestAsString)) {
                    System.out.println("Closing client connection...");
                    return;
                }

                System.out.println("Message from client: " + requestAsString);
                pool.submit(Worker.newWorker(requestAsString, writer));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionHandler newConnectionHandler(Socket socket) {
        return new ConnectionHandler(socket);
    }

}
