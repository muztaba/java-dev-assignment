package socket.server.manager;

import socket.io.RequestObject;

public class ResponseFormatter {

    public static String format(Object result, RequestObject requestObject) {
        return result.toString() + " // n = " + requestObject.getArg("n");
    }

}
