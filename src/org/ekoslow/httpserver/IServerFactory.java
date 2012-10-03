package org.ekoslow.httpserver;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/2/12
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IServerFactory {

    public Server generate(IParser parser, String directory);

}
