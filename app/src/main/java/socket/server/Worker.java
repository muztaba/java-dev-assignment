package socket.server;

import socket.io.RequestObject;
import socket.io.RequestObjectJsonMapper;
import socket.io.Writer;
import socket.server.manager.ManagerService;
import socket.server.manager.ResponseFormatter;

import java.io.ObjectOutputStream;

public class Worker implements Runnable {

    private final String requestAsString;
    private final ObjectOutputStream out;
    private final Writer writer;
    private final ResponseFormatter responseFormatter;

    public Worker(String requestAsString, ObjectOutputStream out, Writer writer, ResponseFormatter responseFormatter) {
        this.requestAsString = requestAsString;
        this.out = out;
        this.writer = writer;
        this.responseFormatter = responseFormatter;
    }

    @Override
    public void run() {
        String result = execute(RequestObjectJsonMapper.readAsObject(requestAsString));
        writer.write(result, out);
    }

    public String execute(RequestObject requestObject) {
        String response;
        try {
            System.out.println("Executing on thread " + Thread.currentThread().getName());
            Object result = ManagerService.of(requestObject).execute();
            response = responseFormatter.format(result, requestObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = "Execution Error!! message: " + e.getMessage();
        }
        System.out.println("Worker Thread name: " + Thread.currentThread().getName() + " response: " + response);
        return response;
    }

    public static WorkerBuilder builder() {
        return new WorkerBuilder();
    }

    public static class WorkerBuilder {
        private String requestAsString;
        private ObjectOutputStream out;
        private Writer writer;
        private ResponseFormatter responseFormatter;

        public WorkerBuilder setRequestAsString(String requestAsString) {
            this.requestAsString = requestAsString;
            return this;
        }

        public WorkerBuilder setObjectOutputStream(ObjectOutputStream out) {
            this.out = out;
            return this;
        }

        public WorkerBuilder setWriter(Writer writer) {
            this.writer = writer;
            return this;
        }

        public WorkerBuilder setResponseFormatter(ResponseFormatter responseFormatter) {
            this.responseFormatter = responseFormatter;
            return this;
        }

        public Worker build() {
            return new Worker(requestAsString, out, writer, responseFormatter);
        }
    }
}
