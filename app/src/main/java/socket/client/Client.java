package socket.client;

import socket.io.RequestObject;
import socket.io.Reader;
import socket.io.RequestObjectJsonMapper;
import socket.io.Writer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Client {

    private final String host;
    private final int port;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Writer writer;
    private Reader reader;

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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void requestToServer() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        writer = new Writer(objectOutputStream);
        reader = new Reader(objectInputStream);
        new Thread(writer).start();
        new Thread(reader).start();

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
            writer.write(RequestObjectJsonMapper.writeAsString(requestObject));
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

    private void exitClient() throws InterruptedException {
        writer.write("EXIT");
        TimeUnit.SECONDS.sleep(2);
        writer.interrupt();
        reader.interrupt();
    }

    private boolean isExit(String isExit) {
        return "y".equalsIgnoreCase(isExit);
    }

    public static Client newClient(String host, int port) {
        return new Client(host, port);
    }

}
