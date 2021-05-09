package socket.client;

import socket.io.RequestObject;
import socket.io.RequestObjectJsonMapper;
import socket.io.Writer;

public class Worker implements Runnable {

    private final RequestObject requestObject;
    private final Writer writer;

    public Worker(RequestObject requestObject, Writer writer) {
        this.requestObject = requestObject;
        this.writer = writer;
    }

    @Override
    public void run() {
        writer.write(RequestObjectJsonMapper.writeAsString(requestObject));
    }

    public static Worker newWorker(RequestObject requestObject, Writer writer) {
        return new Worker(requestObject, writer);
    }
}
