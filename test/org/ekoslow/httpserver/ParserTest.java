package org.ekoslow.httpserver;

import org.junit.*;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/5/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParserTest {

    @Test
    public void readTheCorrectVerb() throws Exception {
        assertEquals("GET", new Parser("GET /index.html HTTP/1.1").verb());
    }

    @Test
    public void readTheCorrectUri() throws Exception {
        assertEquals("/", new Parser("GET / HTTP/1.1").uri());
    }

    @Test
    public void readTheCorrectVersion() throws Exception {
        assertEquals("HTTP/1.1", new Parser("GET /index.html HTTP/1.1").version());
    }

    @Test
    public void createsTheCorrectHeaders() throws Exception {
        String request = "GET / HTTP/1.1\r\nHost: google.com";
        assertEquals("google.com", new Parser(request).headers().get("host"));
    }

    @Test
    public void createsMultiableHeaders() throws Exception {
        String request = "GET / HTTP/1.1\r\nHost: google.com\r\nContent-Length: 10\r\n\r\n";
        Parser parser = new Parser(request);
        assertEquals("google.com", parser.headers().get("host"));
        assertEquals("10", parser.headers().get("content-length"));
    }

    @Test
    public void createsTheProperBody() throws Exception {
        String request = "GET / HTTP/1.1\r\n\r\nBODY\nWith multiple lines";
        assertEquals("BODY\nWith multiple lines", new Parser(request).body());
    }

    @Test
    public void fullHttpRequest() throws Exception {
        String request = "GET /hi HTTP/1.1\nHost: localhost:3000\nConnection: keep-alive\nUser-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.57 Safari/537.1\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept-Encoding: gzip,deflate,sdch\nAccept-Language: en-US,en;q=0.8\nAccept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3\n\n\n";
        assertEquals("GET", new Parser(request).verb());
    }
}
