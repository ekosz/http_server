package org.ekoslow.httpserver.tictactoe;

import org.ekoslow.httpserver.HtmlBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/2/12
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class HtmlBoardBuilder extends HtmlBuilder {
    private String[] board;

    public HtmlBoardBuilder(String[] board) {
        this.board = board;
    }

    public String build() {
        return "<html><head>" + customCss() + "</head><body>" + divTag("board", boardHtml()) + form() + jQueryScript() + customJavascript() + "</body></html>";
    }

    private String form() {
        return "<form action=\"/\" method=\"POST\"><input name=\"move\" /><input name=\"board\" /></form>";
    }

    private String customJavascript() {
        return "<script type=\"text/javascript\" src=\"/javascript/script.js\"></script>";
    }

    private String customCss() {
        return "<link rel=\"stylesheet\" href=\"/css/style.css\" />";
    }

    private String jQueryScript() {
        return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js\"></script>";
    }

    private String boardHtml() {
        return rowHtml(1) + rowHtml(4) + rowHtml(7);
    }

    private String rowHtml(int start) {
        return divTag("row", cellHtml(start) + cellHtml(start+1) + cellHtml(start+2));
    }

    private String cellHtml (int cellNum) {
        return divTag("cell cell"+cellNum, aTag("#", board[cellNum-1] == null ? String.valueOf(cellNum) : board[cellNum-1]));
    }

    private String aTag(String href, String body) {
        return "<a href='"+href+"'>"+body+"</a>";
    }
}
