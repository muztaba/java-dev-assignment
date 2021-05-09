package socket.client;

import socket.io.RequestObject;
import socket.io.RequestObjectJsonMapper;

import java.io.IOException;

public class Worker implements Runnable {

    private final RequestObject requestObject;
    private final SyncIO syncIO;

    public Worker(RequestObject requestObject, SyncIO syncIO) {
        this.requestObject = requestObject;
        this.syncIO = syncIO;
    }

    @Override
    public void run() {
        try {
            syncIO.send(RequestObjectJsonMapper.writeAsString(requestObject));
            String reply = syncIO.received();
            System.out.println("Reply : " + reply);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Worker newWorker(RequestObject requestObject, SyncIO syncIO) {
        return new Worker(requestObject, syncIO);
    }
}
