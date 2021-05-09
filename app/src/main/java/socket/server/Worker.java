package socket.server;

import socket.io.Writer;
import socket.io.RequestObject;
import socket.io.RequestObjectJsonMapper;
import socket.server.manager.ManagerService;
import socket.server.manager.ResponseFormatter;

public class Worker implements Runnable {

    private final String requestAsString;
    private final Writer writer;

    public Worker(String requestAsString, Writer writer) {
        this.requestAsString = requestAsString;
        this.writer = writer;
    }

    @Override
    public void run() {
        String result = execute(RequestObjectJsonMapper.readAsObject(requestAsString));
        writer.write(result);
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

    public static Worker newWorker(String requestAsString, Writer writer) {
        return new Worker(requestAsString, writer);
    }
}
