package httpserver;

import org.junit.*;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/18/12
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */

class SocketMock implements IO {
    public String output;
    public String input;

    public SocketMock(String output) {
       this.output = output;
    }

    @Override
    public String read() {
        return output;
    }

    @Override
    public void write(String input) {
        this.input = input;
    }

    @Override
    public void close() {}
}
public class ServerSpawnerTest {

    @Test
    public void createsProperResponse() throws Exception {
        SocketMock socket = new SocketMock("GET /foobar HTTP/1.1\r\n\r\n");
        ServerSpawner ss = new ServerSpawner(socket, "public");
        ss.run();
        assertEquals("HTTP/1.1 404 Not Found", socket.input.split("\\r?\\n")[0]);
    }
}
