package socket.server;

import socket.io.RequestObject;
import socket.io.RequestObjectJsonMapper;
import socket.server.manager.ManagerService;
import socket.server.manager.ResponseFormatter;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Worker implements Runnable {

    private final ObjectOutputStream objectOutputStream;
    private final String requestAsString;

    public Worker(ObjectOutputStream objectOutputStream, String requestAsString) {
        this.objectOutputStream = objectOutputStream;
        this.requestAsString = requestAsString;
    }

    @Override
    public void run() {

        String result = execute(RequestObjectJsonMapper.readAsObject(requestAsString));
        try {
            objectOutputStream.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String execute(RequestObject requestObject) {
        String response;
        try {
            System.out.println("Executing on thread " + Thread.currentThread().getName());
            response = ResponseFormatter.format(ManagerService.of(requestObject).execute(), requestObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = "Execution Error!! message: " + e.getMessage();
        }
        System.out.println("Worker Thread name: " + Thread.currentThread().getName() + " response: " + response);
        return response;
    }

    public static Worker newWorker(String requestAsString, ObjectOutputStream objectOutputStream) {
        return new Worker(objectOutputStream, requestAsString);
    }
}
