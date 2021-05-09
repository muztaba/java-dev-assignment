package socket.io;

import java.io.Serializable;
import java.util.Map;

public class RequestObject implements Serializable {
    private String managerName;
    private String method;
    private Map<String, String> args;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    public String getArg(String key) {
        return args.get(key);
    }
}
