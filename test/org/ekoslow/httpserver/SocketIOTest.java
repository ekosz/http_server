package org.ekoslow.httpserver;

import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */
class SocketMock extends Socket {

    public OutputStream outputStream;
    private InputStream inputStream;
    public boolean closed = false;

    public SocketMock(String input) throws IOException {
        byte[] stringByte = "".getBytes();
        ByteArrayOutputStream bos = new ByteArrayOutputStream("".length());
        bos.write(stringByte);
        this.outputStream =  bos;

        this.inputStream =  new ByteArrayInputStream(input.getBytes(Charset.defaultCharset()));
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void close() {
        closed = true;
    }

}
public class SocketIOTest {
    @Test
    public void read() throws Exception {
        String text = "GET /foobar HTTP/1.1\r\nHost: google.com\r\nType: text/plain\r\n\r\n";
        assertEquals(text, new SocketIO(new SocketMock(text)).read());
    }

    @Test
    public void write() throws Exception {
        SocketMock socket = new SocketMock("");
        new SocketIO(socket).write("abc123");
        assertEquals("abc123", new String(socket.outputStream.toString()));
    }

    @Test
    public void close() throws Exception {
        SocketMock socket = new SocketMock("");
        new SocketIO(socket).close();
        assertEquals(true, socket.closed);
    }
}
