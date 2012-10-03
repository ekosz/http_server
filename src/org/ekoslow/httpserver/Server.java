package org.ekoslow.httpserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.FileNameMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/2/12
 * Time: 10:58 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Server {
    public String body;
    public HashMap<String, String> headers;
    public int statusCode;
    protected IParser parser;
    protected String directory;
    protected IReader reader;

    public Server(IParser parser, String directory) {
        this.parser = parser;
        this.body = "";
        this.statusCode = 200;
        this.headers = new HashMap<String, String>();
        this.directory = directory;
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
        handleRequest(parser.verb(), uri.getPath(), uri);
        setContentLength();
    }

    protected abstract void handleRequest(String verb, String path, URI uri);

    protected HashMap<String, String> parseQueryString(String query) {
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
        headers.put("Content-Length", String.valueOf(body.length()));
    }

    protected void findFileOrDirectory(String path) {
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

    protected void fourOhFour() {
        this.statusCode = 404;
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
            String ext = extensionOf(file);
            String type = null;
            if(ext.equals("css")) {
                type = "text/css";
            } else if(ext.equals("js")) {
                type = "text/javascript";
            } else {
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                type = fileNameMap.getContentTypeFor(file.getAbsolutePath());
            }
            this.headers.put("Content-Type", type);
        } catch (FileNotFoundException e) {
            this.body = "Could not read file " + file.getAbsolutePath();
            this.statusCode = 500;
        }
    }

    private String extensionOf(File file) {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1)
            ext = s.substring(i+1).toLowerCase();

        if(ext == null)
            return "";
        return ext;
    }
}
