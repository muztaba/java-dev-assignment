package socket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SyncIO {

    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

    private SyncIO(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
    }

    public synchronized void send(String message) throws IOException {
        objectOutputStream.writeObject(message);
    }

    public synchronized String received() throws IOException, ClassNotFoundException {
        return (String) objectInputStream.readObject();
    }

    public static SyncIO of(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        return new SyncIO(objectOutputStream, objectInputStream);
    }
}
