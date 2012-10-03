package org.ekoslow.httpserver;

import org.junit.*;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/11/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EncoderTest {

    @Test
    public void createsBasicResponse() throws Exception {
        assertEquals("HTTP/1.1 200 OK\n\n", new Encoder(200, new HashMap<String, String>(), "").encode());
    }

    @Test
    public void createsResponseWithBody() throws Exception {
        assertEquals("HTTP/1.1 200 OK\n\nBody", new Encoder(200, new HashMap<String, String>(), "Body").encode());
    }

    @Test
    public void createsResponseWithHeaders() throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Content-Length", "10");
        assertEquals("HTTP/1.1 200 OK\nContent-Length: 10\n\n", new Encoder(200, map, "").encode());
    }
}
