package org.ekoslow.httpserver;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 10/2/12
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class HtmlBuilder {

    public abstract String build();

    protected String htmlBodyTag(String body) {
        return "<html><body>"+body+"</body></html>";
    }

    protected String pTag(String body) {
        return "<p>"+body+"</p>";
    }

    protected String divTag(String klass, String body) {
        return "<div class='"+klass+"'>"+body+"</div>";
    }


}
