package socket.utils;

import com.google.common.collect.ImmutableMap;
import socket.io.RequestObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtils {

    public static RequestObject createRequestObject() {
        RequestObject requestObject = new RequestObject();
        requestObject.setMethod("findPrimes");
        requestObject.setManagerName("PrimeCalculationManager");
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 2000 + 1);
        Map<String, String> map = new HashMap<>();
        map.put("n", String.valueOf(randomNum));
        requestObject.setArgs(map);
        return requestObject;
    }

}
