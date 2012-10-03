package org.ekoslow.httpserver;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestMain {
    @Test
    public void hasDefaults() {
        assertEquals(true, new Main(new ServerFactoryMock(), new String[0]).options.get("p") != null);
        assertEquals(true, new Main(new ServerFactoryMock(), new String[0]).options.get("d") != null);
    }

    @Test
    public void canSetPort() {
        assertEquals("5000", new Main(new ServerFactoryMock(), new String[] { "-p", "5000" }).options.get("p"));
    }

    @Test
    public void canSetDirectory() {
        assertEquals("/volume", new Main(new ServerFactoryMock(), new String[] { "-d", "/volume" }).options.get("d"));
    }

    @Test
    public void listenForConnection() throws IOException {
        String text = "GET /foobar HTTP/1.1\r\nHost: google.com\r\nType: text/plain\r\n\r\n";
        SocketMock mock = new SocketMock(text);
        Main main = new Main(new ServerFactoryMock(), new ServerSocketFactoryMock(mock), new String[0]);
        main.setupSocketServer();
        assertEquals(mock, main.listenForConnection());
    }

    @Test
    public void fullStack() throws Exception {
        String text = "GET /foobar HTTP/1.1\r\nHost: google.com\r\nType: text/plain\r\n\r\n";
        SocketMock mock = new SocketMock(text);
        Main main = new Main(new ServerFactoryMock(), new ServerSocketFactoryMock(mock), new String[0]);
        try {
        main.start();
        } catch (Exception e) {
            fail("The full stack threw an error!");
        }
    }

    @Test
    public void handlesBadSocketServer() throws Exception {
        Main main = new Main(new ServerFactoryMock(), new BadServerSocketFactoryMock(), new String[0]);
        main.start();
        assertEquals(null, main.serverSocket);
    }

    @Test
    public void handlesBadSocket() throws Exception {
        Main main = new Main(new ServerFactoryMock(), new ServerSocketThrowsErrorFactoryMock(), new String[0]);
        main.start();
    }

    @Test
    public void handlesBadThreads() throws Exception {
        String text = "GET /foobar HTTP/1.1\r\nHost: google.com\r\nType: text/plain\r\n\r\n";
        SocketMock mock = new SocketMock(text);
        Main main = new Main(new ServerFactoryMock(), new BadThreadFactory(), new String[0]);
        main.startServerSpawnerThread(mock);
    }
}

class ServerSocketFactoryMock extends ServerSocketFactory {

    private SocketMock mock;

    public ServerSocketFactoryMock(SocketMock mock) {
        this.mock = mock;
    }

    @Override
    public ServerSocket generate(int port) throws IOException {
        return new ServerSocketMock(mock);
    }
}

class BadServerSocketFactoryMock extends ServerSocketFactory {

    @Override
    public ServerSocket generate(int port) throws IOException {
        throw new IOException();
    }
}

class ServerSocketThrowsErrorFactoryMock extends ServerSocketFactory {

    @Override
    public ServerSocket generate(int port) throws IOException {
        return new BadServerSocketMock();
    }

}

class ServerSocketMock extends ServerSocket {

    private Socket socket;

    public ServerSocketMock(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public Socket accept() throws IOException {
        return socket;
    }
}

class BadServerSocketMock extends ServerSocket {
    public BadServerSocketMock() throws IOException {
    }

    public Socket accept() throws IOException {
        throw new IOException();
    }
}

class BadThreadFactory extends ThreadFactory {

    @Override
    public Thread generate(Runnable runnable) throws IOException {
        throw new IOException();
    }
}
