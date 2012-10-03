package org.ekoslow.httpserver;

import org.junit.*;

import java.io.IOException;
import java.net.URI;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/18/12
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */

class SocketIOMock implements IO {
    public String output;
    public String input;

    public SocketIOMock(String output) {
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

class BadSocketIOMock implements IO {
    public String input;

    @Override
    public String read() throws IOException {
        throw new IOException();
    }

    @Override
    public void write(String input) {
        this.input = input;
    }

    @Override
    public void close() throws IOException {
        throw new IOException();
    }
}
class ReallyBadSocketIOMock implements IO {
    public String input;

    @Override
    public String read() throws IOException {
        return String.valueOf(1 / 0);
    }

    @Override
    public void write(String input) {
        this.input = input;
    }

    @Override
    public void close() throws IOException {
        throw new IOException();
    }
}
public class ServerSpawnerTest {

    @Test
    public void createsProperResponse() throws Exception {
        SocketIOMock socket = new SocketIOMock("GET /foobar HTTP/1.1\r\n\r\n");
        ServerSpawner ss = new ServerSpawner(socket, "public", new ServerFactoryMock());
        ss.run();
        assertEquals("HTTP/1.1 404 Not Found", socket.input.split("\\r?\\n")[0]);
    }

    @Test
    public void badResponse() throws Exception {
        BadSocketIOMock socket = new BadSocketIOMock();
        ServerSpawner ss = new ServerSpawner(socket, "public", new ServerFactoryMock());
        ss.run();
        assertEquals("HTTP/1.1 500 Internal Server Error", socket.input.split("\\r?\\n")[0]);
    }

    @Test
    public void reallyBadResponse() throws Exception {
        ReallyBadSocketIOMock socket = new ReallyBadSocketIOMock();
        ServerSpawner ss = new ServerSpawner(socket, "public", new ServerFactoryMock());
        ss.run();
        assertEquals("HTTP/1.1 500 Internal Server Error", socket.input.split("\\r?\\n")[0]);
    }
}

class ServerFactoryMock implements IServerFactory {

    @Override
    public Server generate(IParser parser, String directory) {
        return new ServerMock(parser, directory, 404, "");
    }
}

class ServerMock extends Server {
    public ServerMock(IParser parser, String directory, int statusCode, String body) {
        super(parser, directory);
        this.body = body;
        this.statusCode = statusCode;
    }

    @Override
    protected void handleRequest(String verb, String path, URI uri) {  }
}
