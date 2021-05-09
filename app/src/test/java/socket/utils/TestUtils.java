package socket.utils;

import com.google.common.collect.ImmutableMap;
import socket.io.RequestObject;

import java.util.concurrent.ThreadLocalRandom;

public class TestUtils {

    public static RequestObject createRequestObject() {
        RequestObject requestObject = new RequestObject();
        requestObject.setMethod("findPrimes");
        requestObject.setManagerName("PrimeCalculationManager");
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 2000 + 1);
        requestObject.setArgs(ImmutableMap.of("n", String.valueOf(randomNum)));
        return requestObject;
    }

}
