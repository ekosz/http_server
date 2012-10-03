package org.ekoslow.httpserver.cobserver;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */

class CalendarMock extends GregorianCalendar {
    public int get(int type) {
        String toReturn = "";
        if (type == HOUR) {
            toReturn = "2";
        } else if (type == MINUTE) {
            toReturn = "3";
        } else if (type == SECOND) {
            toReturn = "4";
        } else if (type == MILLISECOND) {
            toReturn = "5";
        }
        return Integer.valueOf(toReturn);
    }
}
public class TimeTest {

    @Test
    public void getCurrentTime() {
        assertEquals("2:03:04:005", new Time(new CalendarMock()).currentTime());
    }
}
