package httpserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/24/12
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IReader {

    public String read(File file) throws FileNotFoundException;

    public String read(InputStream stream) throws IOException;
}
