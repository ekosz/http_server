package org.ekoslow.httpserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class Reader implements IReader {
    @Override
    public String read(File file) throws FileNotFoundException {
        return new Scanner(file).useDelimiter("\\Z").next();
    }

    @Override
    public String read(InputStream stream) {
        return new Scanner(stream).useDelimiter("\\Z").next();
    }
}
