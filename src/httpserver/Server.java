package httpserver;

import com.apple.eawt.AppEvent;
import sun.font.FileFont;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
public class Server {
    private IParser parser;
    public String body;
    public int statusCode;
    public HashMap<String, String> headers;
    private String directory;
    private Calendar calendar;
    private IReader reader;
    private Sleeper sleeper;

    public Server(IParser parser, String directoy) {
        this.parser = parser;
        this.body = "";
        this.statusCode = 200;
        this.headers = new HashMap<String, String>();
        this.directory = directoy;
        this.calendar = new GregorianCalendar();
        this.reader = new Reader();
        this.sleeper = new Sleeper();
    }

    public Server(IParser parser, String directory, Calendar calendar) {
        this.parser = parser;
        this.body = "";
        this.statusCode = 200;
        this.headers = new HashMap<String, String>();
        this.directory = directory;
        this.calendar = calendar;
        this.reader = new Reader();
        this.sleeper = new Sleeper();
    }


    public Server(IParser parser, String directory, IReader reader) {
        this.parser = parser;
        this.body = "";
        this.statusCode = 200;
        this.headers = new HashMap<String, String>();
        this.directory = directory;
        this.calendar = new GregorianCalendar();
        this.reader = reader;
        this.sleeper = new Sleeper();
    }

    public Server(IParser parser, String directory, Sleeper sleeper) {
        this.parser = parser;
        this.body = "";
        this.statusCode = 200;
        this.headers = new HashMap<String, String>();
        this.directory = directory;
        this.calendar = new GregorianCalendar();
        this.reader = new Reader();
        this.sleeper = sleeper;
    }

    public void run() {
        URI uri = null;
        try {
            uri = new URI(parser.uri());
        } catch (URISyntaxException e) {
            this.body = "Not a valid URL";
            this.statusCode = 500;
            return;
        }
        if(uri.getPath().equals("/some-script-url") && parser.verb().equals("GET")) {
            outputQueryString(uri);
        } else if (uri.getPath().equals("/hello")  && (parser.verb().equals("GET"))) {
            greet();
        } else if (uri.getPath().equals("/time") && (parser.verb().equals("GET"))) {
            outputTime();
        } else if (uri.getPath().equals("/form") && (parser.verb().equals("GET"))) {
            displayForm(uri);
        } else if (uri.getPath().equals("/form") && (parser.verb().equals("POST") || parser.verb().equals("PUT"))) {
            outputPostData();
        } else {
            findFileOrDirectory(uri.getPath());
        }
        setContentLength();

    }

    private void findFileOrDirectory(String path) {
        String fullPath = directory + path;
        File fileOrDirectory = new File(fullPath);

        if(fileOrDirectory.isFile()) {
            serveFile(fileOrDirectory);
        } else if(fileOrDirectory.isDirectory()) {
            serveDirectory(fileOrDirectory);
        } else {
            fourOhFour();
        }
    }

    private void serveDirectory(File directory) {
        File[] files = directory.listFiles();
        for(File file : files) {
            this.body += "<div><a href=\"" + relativePath(file.getPath()) + "\">" + file.getName() + "</a></div>";
        }
        this.headers.put("Content-Type", "text/html");
    }

    private String relativePath(String path) {
        return path.substring(directory.length(), path.length());
    }

    private void serveFile(File file) {
        try {
            this.body = reader.read(file);
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String type = fileNameMap.getContentTypeFor(file.getAbsolutePath());
            this.headers.put("Content-Type", type);
        } catch (FileNotFoundException e) {
            this.body = "Could not read file " + file.getAbsolutePath();
            this.statusCode = 500;
        }
    }

    private HashMap<String, String> parseQueryString(String query) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (query == null)
            return map;
        String[] params = query.split("&");
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private void setContentLength() {
        this.headers.put("Content-Length", String.valueOf(this.body.length()));
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

    private void fourOhFour() {
        this.statusCode = 404;
    }

    private void outputQueryString(URI uri) {
        Map<String, String> params = parseQueryString(uri.getQuery());
        for (String key : params.keySet()) {
            this.body += key + " = " + params.get(key) + "\n";
        }
    }
}
