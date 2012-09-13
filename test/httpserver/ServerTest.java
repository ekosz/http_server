package httpserver;

import org.junit.*;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/13/12
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
class ParserMock implements IParser {
    private String verb;
    private String uri;
    private String version;
    private HashMap<String, String> headers;
    private String body;

    public ParserMock(String verb, String uri, String version, HashMap<String, String> headers, String body) {
        this.verb = verb;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String verb() {
        return verb;
    }

    @Override
    public String uri() {
        return uri;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public HashMap<String, String> headers() {
        return headers;
    }

    @Override
    public String body() {
        return body;
    }
}

public class ServerTest {

    @Test
    public void echoBackQueryString() throws Exception {
        IParser mock = new ParserMock("GET", "/some-script-url?a=1&b=2", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock);
        server.run();
        assertEquals("b = 2\na = 1\n", server.body);
    }

    @Test
    public void fourOhFour() throws Exception {
        IParser mock = new ParserMock("GET", "/blahblah", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock);
        server.run();
        assertEquals(404, server.statusCode);
    }

    @Test
    public void postContent() throws Exception {
        IParser mock = new ParserMock("POST", "/form", "HTTP/1.1", new HashMap<String, String>(), "\"My\"=\"Data\"");
        Server server = new Server(mock);
        server.run();
        assertEquals("\"My\"=\"Data\"", server.body);
    }

    @Test
    public void simpleGet() throws Exception {
        IParser mock = new ParserMock("GET", "", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock);
        server.run();
        assertEquals(200, server.statusCode);
    }

    @Test
    public void simplePost() throws Exception {
        IParser mock = new ParserMock("POST", "/form", "HTTP/1.1", new HashMap<String, String>(), "\"My\"=\"Data\"");
        Server server = new Server(mock);
        server.run();
        assertEquals(200, server.statusCode);
    }

    @Test
    public void simplePut() throws Exception {
        IParser mock = new ParserMock("PUT", "/form", "HTTP/1.1", new HashMap<String, String>(), "\"My\"=\"Data\"");
        Server server = new Server(mock);
        server.run();
        assertEquals(200, server.statusCode);
    }
}
