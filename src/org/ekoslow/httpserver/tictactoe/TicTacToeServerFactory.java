package org.ekoslow.httpserver.tictactoe;

import org.ekoslow.httpserver.IParser;
import org.ekoslow.httpserver.IServerFactory;
import org.ekoslow.httpserver.Server;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/2/12
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class TicTacToeServerFactory implements IServerFactory {

    @Override
    public Server generate(IParser parser, String directory) {
        return new TicTacToeServer(parser, directory);
    }

}
