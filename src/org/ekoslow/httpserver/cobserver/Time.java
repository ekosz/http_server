package org.ekoslow.httpserver.cobserver;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class Time {
    private Calendar calendar;

    public Time(Calendar calendar) {
        this.calendar = calendar;
    }

    public String currentTime() {
        return time(calendar.get(calendar.HOUR), calendar.get(calendar.MINUTE),
                    calendar.get(calendar.SECOND), calendar.get(calendar.MILLISECOND));
    }

    private String time(Integer hour, Integer min, Integer sec, Integer mili) {
        return hour + ":" + padded(min) + ":" + padded(sec) + ":" + padded(mili, 3);
    }

    private String padded(Integer time) {
        return padded(time, 2);
    }

    private String padded(Integer time, Integer length) {
        String s = String.valueOf(time);
        while (s.length() < length) {
            s = "0" + s;
        }
        return s;
    }

}
