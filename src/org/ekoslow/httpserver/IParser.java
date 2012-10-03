package org.ekoslow.httpserver;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/13/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IParser {

    public String verb();

    public String uri();

    public String version();

    public HashMap<String, String> headers();

    public String body();
}
