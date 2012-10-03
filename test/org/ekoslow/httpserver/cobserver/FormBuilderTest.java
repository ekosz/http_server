package org.ekoslow.httpserver.cobserver;

import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/25/12
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class FormBuilderTest {

    @Test
    public void buildsPlainForm() throws Exception {
        File file = new File("test/form.html");
        String fileContents = new Scanner(file).useDelimiter("\\Z").next();
        assertEquals(fileContents, new FormBuilder(new HashMap<String, String>()).build());
    }

    @Test
    public void buildsFormWithOptions() throws Exception {
        File file = new File("test/formWithOptions.html");
        String fileContents = new Scanner(file).useDelimiter("\\Z").next();
        HashMap<String, String> map = new HashMap<String, String>() {
            {
                put("a", "1");
                put("b", "2");
                put("c", "3");
            }
        };
        assertEquals(fileContents, new FormBuilder(map).build());
    }
}
