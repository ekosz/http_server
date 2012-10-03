package org.ekoslow.httpserver.cobserver;

import org.ekoslow.httpserver.*;

import java.net.*;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/13/12
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class CobServer extends Server {
    private Calendar calendar;
    private Sleeper sleeper;

    public CobServer(IParser parser, String directory) {
        super(parser, directory);
        this.calendar = new GregorianCalendar();
        this.reader = new Reader();
        this.sleeper = new Sleeper();
    }

    public CobServer(IParser parser, String directory, Calendar calendar) {
        super(parser, directory);
        this.calendar = calendar;
        this.reader = new Reader();
        this.sleeper = new Sleeper();
    }


    public CobServer(IParser parser, String directory, IReader reader) {
        super(parser, directory);
        this.calendar = new GregorianCalendar();
        this.reader = reader;
        this.sleeper = new Sleeper();
    }

    public CobServer(IParser parser, String directory, Sleeper sleeper) {
        super(parser, directory);
        this.directory = directory;
        this.calendar = new GregorianCalendar();
        this.reader = new Reader();
        this.sleeper = sleeper;
    }

    @Override
    protected void handleRequest(String verb, String path, URI uri) {
        if(path.equals("/some-script-url") && verb.equals("GET")) {
            outputQueryString(uri);
        } else if (path.equals("/hello")  && (verb.equals("GET"))) {
            greet();
        } else if (path.equals("/time") && (verb.equals("GET"))) {
            outputTime();
        } else if (path.equals("/form") && (verb.equals("GET"))) {
            displayForm(uri);
        } else if (path.equals("/form") && (verb.equals("POST") || verb.equals("PUT"))) {
            outputPostData();
        } else {
            findFileOrDirectory(path);
        }
    }


    private void displayForm(URI uri) {
        FormBuilder builder = new FormBuilder(parseQueryString(uri.getQuery()));
        this.body = builder.build();
    }

    private void greet() {
        this.body = "<h1>Welcome to the Project</h1>";
    }

    private void outputTime() {
        try {
            sleeper.sleep(1000);
        } catch (InterruptedException e) {
            this.statusCode = 500;
            this.body = "Interrupted!";
            return;
        }
        this.body = new Time(calendar).currentTime();
    }

    private void outputPostData() {
        String toReturn = "<ul>";
        for( Map.Entry entry : parseQueryString(parser.body()).entrySet()) {
            toReturn += "<li>"+entry.getKey()+": "+entry.getValue()+"</li>";
        }
        toReturn += "</ul>";
        this.body = toReturn;
        this.headers.put("Content-Type", "text/html");
    }

    private void outputQueryString(URI uri) {
        Map<String, String> params = parseQueryString(uri.getQuery());
        for (String key : params.keySet()) {
            this.body += key + " = " + params.get(key) + "\n";
        }
    }
}
