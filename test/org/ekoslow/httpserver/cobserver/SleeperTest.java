package org.ekoslow.httpserver.cobserver;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SleeperTest {
    @Test
    public void sleeps() throws Exception {
        long now = System.currentTimeMillis();
        new Sleeper().sleep(1000);
        assertEquals(true, (System.currentTimeMillis() >= now + 1000));
    }
}
