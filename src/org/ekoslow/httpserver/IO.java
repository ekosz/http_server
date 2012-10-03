package org.ekoslow.httpserver;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/18/12
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IO {
    public String read() throws IOException;

    public void write(String input);

    public void close() throws IOException;
}
