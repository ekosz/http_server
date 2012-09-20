package httpserver;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.HashMap;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/5/12
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Parser implements IParser {

    private String uri;
    private String version;
    private String verb;
    private String body;
    private HashMap<String, String> headers = new HashMap<String, String>();

    public Parser(String request) {
        String lines[] = request.split("\\r?\\n");
        parseInitialLine(lines[0]);
        parseHeaders(Arrays.copyOfRange(lines, 1, lines.length));
    }

    private void parseHeaders(String[] strings) {
        if(strings.length == 0) return;
        int i;
        for (i=0; i < strings.length; i++) {
            String line = strings[i];
            if (Pattern.matches("^\\s*$", line))
                break;
            String keyVal[] = line.split(":");
            this.headers.put(keyVal[0].toLowerCase(), keyVal[1].trim());
        }
        if(i == strings.length) return;
        parseBody(Arrays.copyOfRange(strings, ++i, strings.length));
    }

    private void parseBody(String[] strings) {
        this.body = combine(strings, "\n");
    }

    private void parseInitialLine(String line) {
        String sections[] = line.split(" ");
        this.verb = sections[0].toUpperCase();
        this.uri = sections[1];
        this.version = sections[2];
    }

    public String verb() {
        return verb;
    }

    public String uri() {
        return uri;
    }

    public String version() {
        return version;
    }

    public HashMap<String, String> headers() {
        return headers;
    }

    public String body() {
        return body;
    }

    private String combine(String[] s, String glue) {
        int k = s.length;
        if (k == 0)
            return null;
        StringBuilder out = new StringBuilder();
        out.append(s[0]);
        for (int x = 1; x < k; ++x)
            out.append(glue).append(s[x]);
        return out.toString();
    }
}
