package org.ekoslow.httpserver.tictactoe;

import org.ekoslow.httpserver.IParser;
import org.ekoslow.httpserver.Main;
import org.ekoslow.httpserver.Reader;
import org.ekoslow.httpserver.Server;
import org.ekoslow.tictactoe.ComputerPlayer;
import org.ekoslow.tictactoe.Game;
import org.ekoslow.tictactoe.SingleMovePlayer;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/2/12
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class TicTacToeServer extends Server {

    public static void main(String[] args) {
        Main main = new Main(new TicTacToeServerFactory(), args);
        main.start();
    }

    public TicTacToeServer(IParser parser) {
        super(parser, "public");
        this.reader = new Reader();
    }

    public TicTacToeServer(IParser parser, String directory) {
        super(parser, directory);
        this.reader = new Reader();
    }

    @Override
    protected void handleRequest(String verb, String path, URI uri) {
        if(verb.equals("GET")) {
            if(path.equals("/")) {
                displayStartGame();
            } else {
                findFileOrDirectory(path);
            }
        } else {
            HashMap<String, String> map = parseQueryString(parser.body());

            Game game = setupGame(map);

            game.playNextTurn();

            displayGame(game);
        }
    }

    private void displayGame(Game game) {
        body = new HtmlBoardBuilder(game.getGrid()).build();
        if(game.isOver()) {
            if(game.isSolved())
                body += "<h1>"+game.winner()+" won!</h1>";
            body += "<a href='/'>Play again?</a>";
        }
    }

    private Game setupGame(HashMap<String, String> map) {
        int move = Integer.parseInt(map.get("move"));
        String[] grid = parseBoard(map.get("board"));

        return new Game(grid, new SingleMovePlayer("o", move), new ComputerPlayer("x"));
    }

    private String[] parseBoard(String board) {
        String[] array = new String[0];
        try {
            board = URLDecoder.decode(board, "UTF-8");
            array = board.substring(1, board.length()-1).replaceAll("\"", "").split(",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        for(int i=0; i < array.length; i++) {
            if(array[i].equals("null")) array[i] = null;
        }
        return array;
    }

    private void displayStartGame() {
        body = new HtmlBoardBuilder(new String[9]).build();
    }
}
