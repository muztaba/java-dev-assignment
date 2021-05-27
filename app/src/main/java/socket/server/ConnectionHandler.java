package socket.server;

import socket.io.Writer;
import socket.server.manager.ResponseFormatter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ConnectionHandler implements Runnable {

    private static final String EXIT = "EXIT";

    private final Socket socket;
    private final ExecutorService workerPool;
    private final Writer writer;

    private ConnectionHandler(Socket socket, ExecutorService workerPool, Writer writer) {
        this.socket = socket;
        this.workerPool = workerPool;
        this.writer = writer;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
        ) {
            while (true) {
                String requestAsString = (String) objectInputStream.readObject();

                if (requestToExit(requestAsString)) {
                    System.out.println("Closing client connection...");
                    return;
                }

                System.out.println("Message from client: " + requestAsString);

                Worker worker = Worker.builder()
                        .setRequestAsString(requestAsString)
                        .setObjectOutputStream(objectOutputStream)
                        .setWriter(writer)
                        .setResponseFormatter(ResponseFormatter.FORMATTER_1)
                        .build();

                workerPool.submit(worker);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeSocket();
        }
    }

    private boolean requestToExit(String requestAsString) {
        return EXIT.equalsIgnoreCase(requestAsString);
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionHandler of(Socket socket, ExecutorService workerPool, Writer writer) {
        return new ConnectionHandler(socket, workerPool, writer);
    }

}
