package httpserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/25/12
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class FormBuilder {

    private Map<String, String> map;

    public FormBuilder(HashMap<String, String> map) {
        this.map = map;
    }

    public String build() {
        return htmlBodyTag(formTag(input("a")+input("b")+input("c")+pTag("<input type='submit' />")));
    }

    private String htmlBodyTag(String body) {
        return "<html><body>"+body+"</body></html>";
    }

    private String formTag(String body) {
        return "<form action='/form' method='POST'>"+body+"</form>";
    }

    private String input(String name) {
       return pTag(labelTag(name)+inputTag(name));
    }

    private String pTag(String body) {
        return "<p>"+body+"</p>";
    }

    private String labelTag(String name) {
        return "<label for='"+name+"'>"+name+": </label>";
    }

    private String inputTag(String name) {
        String toReturn = "";
        toReturn += "<input id='"+name+"' name='"+name+"' ";
        String value = map.get(name);
        if (value != null)
            toReturn += "value='"+value+"' ";
        toReturn += "type='text' />";
        return toReturn;
    }


}
