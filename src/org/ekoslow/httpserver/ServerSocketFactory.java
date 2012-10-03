package org.ekoslow.httpserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/1/12
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerSocketFactory {
    public ServerSocket generate(int port) throws IOException {
        return new ServerSocket(port);
    }
}
