package org.ekoslow.httpserver;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/1/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadFactory {
    public Thread generate(Runnable runnable) throws IOException {
        return new Thread(runnable);
    }
}
