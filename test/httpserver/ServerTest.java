package httpserver;

import org.junit.*;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

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
        Server server = new Server(mock, "public");
        server.run();
        assertEquals("b = 2\na = 1\n", server.body);
    }

    @Test
    public void fourOhFour() throws Exception {
        IParser mock = new ParserMock("GET", "/blahblah", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals(404, server.statusCode);
    }

    @Test
    public void getForm() throws Exception {
        File file = new File("src/form.html");
        String fileContents = new Scanner(file).useDelimiter("\\Z").next();
        IParser mock = new ParserMock("GET", "/form", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals(fileContents, server.body);
    }

    @Test
    public void properContentLength() throws Exception {
        File file = new File("src/form.html");
        String fileContents = new Scanner(file).useDelimiter("\\Z").next();
        IParser mock = new ParserMock("GET", "/form", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals(String.valueOf(fileContents.length()), server.headers.get("Content-Length"));
    }

    @Test
    public void welcomeToTheProject() throws Exception {
        IParser mock = new ParserMock("GET", "/hello", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals("<h1>Welcome to the Project</h1>", server.body);
    }

    @Test
    public void postContent() throws Exception {
        IParser mock = new ParserMock("POST", "/form", "HTTP/1.1", new HashMap<String, String>(), "\"My\"=\"Data\"");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals("\"My\"=\"Data\"", server.body);
    }

    @Test
    public void simpleGet() throws Exception {
        IParser mock = new ParserMock("GET", "/", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals(200, server.statusCode);
    }

    @Test
    public void simplePost() throws Exception {
        IParser mock = new ParserMock("POST", "/form", "HTTP/1.1", new HashMap<String, String>(), "\"My\"=\"Data\"");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals(200, server.statusCode);
    }

    @Test
    public void simplePut() throws Exception {
        IParser mock = new ParserMock("PUT", "/form", "HTTP/1.1", new HashMap<String, String>(), "\"My\"=\"Data\"");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals(200, server.statusCode);
    }

    @Test
    public void serveFile() {
        IParser mock = new ParserMock("GET", "/hello.txt", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals("Hello World!", server.body);
        assertEquals("text/plain", server.headers.get("Content-Type"));
    }

    @Test
    public void serveDirectory() {
        IParser mock = new ParserMock("GET", "/", "HTTP/1.1", new HashMap<String, String>(), "");
        Server server = new Server(mock, "public");
        server.run();
        assertEquals("<div><a href=\"/go_here\">go_here</a></div><div><a href=\"/hello.txt\">hello.txt</a></div>",
                     server.body);
        assertEquals("text/html", server.headers.get("Content-Type"));
    }
}
