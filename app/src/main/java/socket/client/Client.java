package socket.client;

import socket.io.RequestObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

    private final String host;
    private final int port;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private SyncIO syncIO;

    private Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            System.out.println("Client port is: " + socket.getPort());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.syncIO = SyncIO.of(objectOutputStream, objectInputStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void requestToServer() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            RequestObject requestObject = new RequestObject();
            System.out.println("Manager name");
            String managerName = scanner.nextLine();
            requestObject.setManagerName(managerName);

            System.out.println("Method Name");
            String methodName = scanner.nextLine();
            requestObject.setMethod(methodName);

            Map<String, String> param = new HashMap<>();
            System.out.println("Enter argName and parameter(example : n 100)." +
                    "Type `- -` to end parameter input");

            while (true) {
                String paramName = scanner.next();
                String argument = scanner.next();
                if ("-".equals(paramName) && "-".equals(argument)) {
                    break;
                }
                param.put(paramName, argument);
            }

            requestObject.setArgs(param);
            new Thread(Worker.newWorker(requestObject, syncIO)).start();
            scanner.nextLine(); // consume left over newline

            System.out.println("Want to exit? Y/N");
            String isExit = scanner.nextLine();
            if (isExit(isExit)) {
                exitClient();
                socket.close();
                return;
            }
        }
    }

    private void exitClient() throws IOException {
        objectOutputStream.writeObject("EXIT");
    }

    private boolean isExit(String isExit) {
        return "y".equalsIgnoreCase(isExit);
    }

    public static Client newClient(String host, int port) {
        return new Client(host, port);
    }

}
