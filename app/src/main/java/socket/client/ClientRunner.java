package socket.client;

import java.io.IOException;

public class ClientRunner {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = Client.newClient("localhost", 9876);
        if (client.connect()) {
            System.out.println("Connect successful");
            client.requestToServer();
        } else {
            System.out.println("Connect failed!!");
        }
    }

}
