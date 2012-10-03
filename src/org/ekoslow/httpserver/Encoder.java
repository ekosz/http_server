package org.ekoslow.httpserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/11/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Encoder {

    private String body;
    private HashMap<String, String> headers;
    private Integer statusCode;

    private final HashMap<Integer, String> statusMessages = new HashMap<Integer, String>() {
        {
            put(200, "OK");
            put(404, "Not Found");
            put(500, "Internal Server Error");
        }
    };

    public Encoder(Integer statusCode, HashMap<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public String encode() {
      return initialLine() + headers() + body();
    }

    private String initialLine() {
        return "HTTP/1.1 " + statusCode + " " + statusMessages.get(statusCode) + "\n";
    }

    private String body() {
        return "\n" + body;
    }

    private String headers() {
        String output = "";
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            output += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return output;
    }
}
