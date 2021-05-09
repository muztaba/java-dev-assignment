package socket.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestObjectJsonMapper {

    public static RequestObject readAsObject(String jsonStr) {
        try {
            return new ObjectMapper().readValue(jsonStr, RequestObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String writeAsString(RequestObject requestObject) {
        try {
            return new ObjectMapper().writeValueAsString(requestObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
