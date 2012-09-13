package httpserver;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;

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

    public Server(IParser parser) {
        this.parser = parser;
        this.body = "";
        this.statusCode = 200;
    }

    private HashMap<String, String> parseQueryString(String query) {
        String[] params = query.split("&");
        HashMap<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    public void run() throws URISyntaxException {
        URI uri = new URI(parser.uri());
        if(uri.getPath().equals("/some-script-url") && parser.verb().equals("GET")) {
            outputQueryString(uri);
        } else if (uri.getPath().equals("") && parser.verb().equals("GET")) {

        } else if (uri.getPath().equals("/form") && (parser.verb().equals("POST") || parser.verb().equals("PUT"))) {
            outputPostData();
        } else {
            fourOhFour();
        }

    }

    private void outputPostData() {
        this.body = parser.body();
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
