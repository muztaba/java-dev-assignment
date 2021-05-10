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
    private Thread writerThread;
    private Thread readerThread;

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
        startReaderAndWriterThread();

        while (true) {
            if (wantToExitApplication(scanner)) {
                return;
            }

            RequestObject requestObject = takeInput(scanner);
            writer.write(RequestObjectJsonMapper.writeAsString(requestObject));
        }
    }

    private void startReaderAndWriterThread() {
        writer = new Writer(objectOutputStream);
        reader = new Reader(objectInputStream);
        writerThread = new Thread(writer);
        readerThread = new Thread(reader);
        writerThread.start();
        readerThread.start();
    }

    private boolean wantToExitApplication(Scanner scanner) throws IOException, InterruptedException {
        System.out.println("Want to continue? Y/N");
        String isExit = scanner.nextLine();
        if (isExit(isExit)) {
            exitClient();
            return true;
        }
        return false;
    }

    private RequestObject takeInput(Scanner scanner) {
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

        scanner.nextLine(); // consume left over newline
        requestObject.setArgs(param);
        return requestObject;
    }

    private void exitClient() throws IOException, InterruptedException {
        writer.write("EXIT");
        writerThread.interrupt();
        readerThread.interrupt();
        TimeUnit.SECONDS.sleep(2);
        objectInputStream.close();
        objectOutputStream.close();
        socket.close();
    }

    private boolean isExit(String isExit) {
        return "n".equalsIgnoreCase(isExit);
    }

    public static Client newClient(String host, int port) {
        return new Client(host, port);
    }

}
