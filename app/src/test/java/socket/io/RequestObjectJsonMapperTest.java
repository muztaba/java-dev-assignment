package socket.io;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestObjectJsonMapperTest {

    @Test
    public void testReadAsObject() {
        String requestObjectAsString = "{\n" +
                "  \"method\":\"findPrimes\",\n" +
                "  \"managerName\":\"PrimeCalculationManager\",\n" +
                "  \"args\":{\"n\":\"100\"}\n" +
                "}";

        RequestObject requestObject = RequestObjectJsonMapper.readAsObject(requestObjectAsString);

        assertEquals(requestObject.getMethod(), "findPrimes");
        assertEquals(requestObject.getManagerName(), "PrimeCalculationManager");
        assertEquals(requestObject.getArgs().size(), 1);
        assertEquals(requestObject.getArgs().get("n"), "100");
    }

    @Test
    public void testWriteAsString() {
        RequestObject actual = new RequestObject();
        actual.setMethod("findPrimes");
        actual.setManagerName("PrimeCalculationManager");
        actual.setArgs(ImmutableMap.of("n", "100"));

        String asString = RequestObjectJsonMapper.writeAsString(actual);
        RequestObject expected = RequestObjectJsonMapper.readAsObject(asString);

        assertEquals(expected.getMethod(), actual.getMethod());
        assertEquals(expected.getManagerName(), actual.getManagerName());
        assertEquals(expected.getArgs().get("n"), actual.getArgs().get("n"));
    }
}