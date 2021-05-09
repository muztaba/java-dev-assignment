package socket.server;

import socket.io.RequestObjectJsonMapper;
import socket.server.manager.ManagerService;

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
                String result = execute(requestAsString);
                objectOutputStream.writeObject(result);
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String execute(String message) {
        String result;
        try {
            result = String.valueOf(ManagerService.of(RequestObjectJsonMapper.readAsObject(message)).execute());
        } catch (Exception e) {
            e.printStackTrace();
            result = "Execution Error!! message: " + e.getMessage();
        }
        return result;
    }

    public static ConnectionHandler newConnectionHandler(Socket socket) {
        return new ConnectionHandler(socket);
    }

}
