package socket.server.manager;

import socket.io.RequestObject;

public interface FormatterFunction {

    String format(Object message, RequestObject requestObject);

}
