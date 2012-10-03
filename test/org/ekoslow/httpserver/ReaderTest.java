package org.ekoslow.httpserver;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.Charset;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReaderTest {

    @Test
    public void readFile() throws Exception {
        assertEquals("Hello World!", new Reader().read(new File("public/hello.txt")));
    }

    @Test
    public void readStream() throws Exception {
        String text = "Hello World!";
        assertEquals(text, new Reader().read(new ByteArrayInputStream(text.getBytes(Charset.defaultCharset()))));
    }
}
