package socket.server.manager;

import socket.io.RequestObject;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ManagerService {

    private static final String MANAGERS_PACKAGE = "socket.server.manager.";

    private final RequestObject requestObject;

    private Class<?> manager;
    private Object instance;
    private Method method;
    private Object[] arguments;

    private ManagerService(RequestObject requestObject) {
        this.requestObject = requestObject;
    }

    public Object execute() {
        init();
        return invokeTargetMethod();
    }

    private void init() {
        if (arguments == null) {
            try {
                initClassObject();
                createNewInstance();
                findTargetMethod();
                prepareTargetMethodArguments();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
               throw  new RuntimeException(e);
            }
        }
    }

    private void initClassObject() throws ClassNotFoundException {
        manager = Class.forName(MANAGERS_PACKAGE + requestObject.getManagerName());
    }

    private void createNewInstance() throws InstantiationException, IllegalAccessException {
        instance = manager.newInstance();
    }

    private void findTargetMethod() {
        int argsSize = requestObject.getArgs().size();

        for (Method method : manager.getMethods()) {
            if (method.getName().equals(requestObject.getMethod()) && (method.getParameters().length == argsSize)) {
                this.method = method;
                break;
            }
        }

        if (method == null) {
            throw new RuntimeException("No method match for given methodName or number of args");
        }
    }

    private void prepareTargetMethodArguments() {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            String paramName = parameters[i].getAnnotation(ParamName.class).name();
            String param = requestObject.getArg(paramName);
            // Should be add more type check.
            // But for the sake of this task, ignoring those
            if (int.class.equals(parameters[i].getType())) {
                args[i] = Integer.parseInt(param);
            }
        }

        this.arguments = args;
    }

    private Object invokeTargetMethod() {
        try {
            return method.invoke(instance, arguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ManagerService of(RequestObject requestObject) {
        return new ManagerService(requestObject);
    }

}
