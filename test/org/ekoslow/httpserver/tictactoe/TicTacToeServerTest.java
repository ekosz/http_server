package org.ekoslow.httpserver.tictactoe;

import org.ekoslow.httpserver.IParser;
import org.ekoslow.httpserver.Parser;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/2/12
 * Time: 9:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class TicTacToeServerTest {

    @Test
    public void newGameHasBoard() {
        IParser mock = new ParserMock("GET", "/", "HTTP/1.1", new HashMap<String, String>(), "");
        TicTacToeServer server = new TicTacToeServer(mock);
        server.run();
        assertTrue(doesContain(server.body, cellHtml("1")));
        assertTrue( doesContain(server.body, cellHtml("9")) );
    }

    @Test
    public void makesMoves() {
        IParser mock = new ParserMock("POST", "/", "HTTP/1.1", new HashMap<String, String>(), "move=1&board=[null,null,null,null,null,null,null,null,null]");
        TicTacToeServer server = new TicTacToeServer(mock);
        server.run();
        assertTrue( doesContain(server.body, cellHtml("1", "o")) );
        assertTrue( doesContain(server.body, cellHtml("5", "x")));
    }

    @Test
    public void handlesUrlEncodedParams() {
        IParser mock = new ParserMock("POST", "/", "HTTP/1.1", new HashMap<String, String>(), "move=1&board=%5Bnull%2Cnull%2Cnull%2Cnull%2Cnull%2Cnull%2Cnull%2Cnull%2Cnull%5D");
        TicTacToeServer server = new TicTacToeServer(mock);
        server.run();
        assertTrue( doesContain(server.body, cellHtml("1", "o")) );
        assertTrue( doesContain(server.body, cellHtml("5", "x")));
    }

    @Test
    public void endOfGame() {
        IParser mock = new ParserMock("POST", "/", "HTTP/1.1", new HashMap<String, String>(), "move=1&board=[null,\"o\",\"x\",\"x\",\"o\",\"x\",\"o\",\"x\",\"o\"]");
        TicTacToeServer server = new TicTacToeServer(mock);
        server.run();
        assertTrue( doesContain(server.body, "<h1>o won!</h1>"));
        assertTrue( doesContain(server.body, "<a href='/'>Play again?</a>") );
    }

    private String cellHtml(String cellNum) {
        return "<div class='cell cell"+cellNum+"'><a href='#'>"+cellNum+"</a></div>";
    }

    private String cellHtml(String cellNum, String value) {
        return "<div class='cell cell"+cellNum+"'><a href='#'>"+value+"</a></div>";
    }

    private boolean doesContain(String body, String toFind) {
        return body.indexOf(toFind) > 0;
    }
}
class ParserMock extends Parser {
    private String verb;
    private String uri;
    private String version;
    private HashMap<String, String> headers;
    private String body;

    public ParserMock(String verb, String uri, String version, HashMap<String, String> headers, String body) {
        this.verb = verb;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String verb() {
        return verb;
    }

    @Override
    public String uri() {
        return uri;
    }

    @Override
    public String body() {
        return body;
    }
}