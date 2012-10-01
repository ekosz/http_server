package httpserver;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sleeper {
    public void sleep(long time) throws InterruptedException {
        Thread.sleep(time);
    }
}
