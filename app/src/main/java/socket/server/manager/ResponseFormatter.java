package socket.server.manager;

import socket.io.RequestObject;

public enum ResponseFormatter implements FormatterFunction {

    FORMATTER_1((message, requestObject) -> message + " // n = " + requestObject.getArg("n"));

    private final FormatterFunction formatterFunction;

    ResponseFormatter(FormatterFunction formatterFunction) {
        this.formatterFunction = formatterFunction;
    }

    @Override
    public String format(Object message, RequestObject requestObject) {
        return formatterFunction.format(message, requestObject);
    }
}
