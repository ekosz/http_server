package org.ekoslow.httpserver.cobserver;

import org.ekoslow.httpserver.IParser;
import org.ekoslow.httpserver.IReader;
import org.ekoslow.httpserver.Parser;
import org.junit.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/13/12
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
class ParserMock extends Parser {
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
    public String body() {
        return body;
    }
}

class ReaderMock implements IReader {

    @Override
    public String read(File file) throws FileNotFoundException {
        throw new FileNotFoundException();
    }

    @Override
    public String read(InputStream stream) throws IOException {
        throw new IOException();
    }
}

class SleeperMock extends Sleeper {

    @Override
    public void sleep(long time) throws InterruptedException {
        throw new InterruptedException();
    }
}

public class CobServerTest {

    @Test
    public void malformedURI() throws Exception {
        IParser mock = new ParserMock("GET", "/[]", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals("Not a valid URL", cobServer.body);
        assertEquals(500, cobServer.statusCode);
    }

    @Test
    public void echoBackQueryString() throws Exception {
        IParser mock = new ParserMock("GET", "/some-script-url?a=1&b=2", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals("b = 2\na = 1\n", cobServer.body);
    }

    @Test
    public void fourOhFour() throws Exception {
        IParser mock = new ParserMock("GET", "/blahblah", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals(404, cobServer.statusCode);
    }

    @Test
    public void getForm() throws Exception {
        File file = new File("test/form.html");
        String fileContents = new Scanner(file).useDelimiter("\\Z").next();
        IParser mock = new ParserMock("GET", "/form", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals(fileContents, cobServer.body);
    }

    @Test
    public void getFromWithOptions() throws Exception {
        IParser mock = new ParserMock("GET", "/form?a=1", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals(true, cobServer.body.indexOf("<input id='a' name='a' value='1' type='text' />") > 0);
    }

    @Test
    public void properContentLength() throws Exception {
        File file = new File("test/form.html");
        String fileContents = new Scanner(file).useDelimiter("\\Z").next();
        IParser mock = new ParserMock("GET", "/form", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals(String.valueOf(fileContents.length()), cobServer.headers.get("Content-Length"));
    }

    @Test
    public void welcomeToTheProject() throws Exception {
        IParser mock = new ParserMock("GET", "/hello", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals("<h1>Welcome to the Project</h1>", cobServer.body);
    }

    @Test
    public void simpleGet() throws Exception {
        IParser mock = new ParserMock("GET", "/", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals(200, cobServer.statusCode);
    }

    @Test
    public void simplePost() throws Exception {
        IParser mock = new ParserMock("POST", "/form", "HTTP/1.1", new HashMap<String, String>(), "\"My\"=\"Data\"");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals(200, cobServer.statusCode);
    }

    @Test
    public void makesListFromPostArguments() throws Exception {
        IParser mock = new ParserMock("POST", "/form", "HTTP/1.1", new HashMap<String, String>(), "a=1");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals(true, cobServer.body.indexOf("<li>a: 1</li>") >= 0);
        assertEquals("text/html", cobServer.headers.get("Content-Type"));
    }

    @Test
    public void simplePut() throws Exception {
        IParser mock = new ParserMock("PUT", "/form", "HTTP/1.1", new HashMap<String, String>(), "\"My\"=\"Data\"");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals(200, cobServer.statusCode);
    }

    @Test
    public void serveFile() {
        IParser mock = new ParserMock("GET", "/hello.txt", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals("Hello World!", cobServer.body);
        assertEquals("text/plain", cobServer.headers.get("Content-Type"));
    }

    @Test
    public void serveJavascriptFile() {
        IParser mock = new ParserMock("GET", "/javascript/script.js", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals("text/javascript", cobServer.headers.get("Content-Type"));
    }

    @Test
    public void serveCssFile() {
        IParser mock = new ParserMock("GET", "/css/style.css", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertEquals("text/css", cobServer.headers.get("Content-Type"));
    }

    @Test
    public void serveBadFile() {
        IParser mock = new ParserMock("GET", "/hello.txt", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public", new ReaderMock());
        cobServer.run();
        assertEquals(500, cobServer.statusCode);
    }

    @Test
    public void serveDirectory() {
        IParser mock = new ParserMock("GET", "/", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public");
        cobServer.run();
        assertTrue(cobServer.body.indexOf("<div><a href=\"/go_here\">go_here</a></div>") > 0);
        assertEquals("text/html", cobServer.headers.get("Content-Type"));
    }

    @Test
    public void outputTime() {
        IParser mock = new ParserMock("GET", "/time", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public", new CalendarMock());
        cobServer.run();
        assertEquals("2:03:04:005", cobServer.body);
    }

    @Test
    public void handlesInterupt() throws Exception {
        IParser mock = new ParserMock("GET", "/time", "HTTP/1.1", new HashMap<String, String>(), "");
        CobServer cobServer = new CobServer(mock, "public", new SleeperMock());
        cobServer.run();
        assertEquals(500, cobServer.statusCode);
    }
}
