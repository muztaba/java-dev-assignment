package socket.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            while (true) {
                String requestAsString = (String) objectInputStream.readObject();

                if (EXIT.equalsIgnoreCase(requestAsString)) {
                    System.out.println("Closing client connection...");
                    socket.close();
                    return;
                }

                System.out.println("Message from client: " + requestAsString);
                Worker worker = Worker.newWorker(requestAsString, objectOutputStream);
                new Thread(worker).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionHandler newConnectionHandler(Socket socket) {
        return new ConnectionHandler(socket);
    }

}
