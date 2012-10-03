package org.ekoslow.httpserver.tictactoe;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/2/12
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class HtmlBoardBuilderTest {

    @Test
    public void buildsEmptyBoard() {
        String[] board = new String[9];
        String htmlBoard = new HtmlBoardBuilder(board).build();
        for(int i=1; i <= 9; i++) {
            assertTrue( hasCell(htmlBoard, i, String.valueOf(i)) );
        }
    }

    @Test
    public void buildsBoardWithValues() {
        String[] board = new String[] {null, "x", null, "o", null, null, null, null, null};
        String htmlBoard = new HtmlBoardBuilder(board).build();
        assertTrue( hasCell(htmlBoard, 1, "1") );
        assertTrue( hasCell(htmlBoard, 2, "x") );
        assertTrue( hasCell(htmlBoard, 4, "o") );
        assertTrue( hasCell(htmlBoard, 5, "5") );
    }

    private boolean hasCell(String htmlBoard, int cellNum, String value) {
        return htmlBoard.indexOf("<div class='cell cell"+cellNum+"'><a href='#'>"+value+"</a></div>") > 0;
    }
}
