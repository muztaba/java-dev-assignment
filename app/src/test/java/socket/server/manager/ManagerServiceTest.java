package socket.server.manager;

import org.junit.jupiter.api.Test;
import socket.io.RequestObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static socket.utils.TestUtils.createRequestObject;

class ManagerServiceTest {

    @Test
    void execute() {
        RequestObject requestObject = createRequestObject();
        ManagerService managerService = ManagerService.of(requestObject);
        Object expected = managerService.execute();

        assertEquals(expected, 25);
    }
}