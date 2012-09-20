package httpserver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/18/12
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerSpawner implements Runnable{
    private IO io;
    private String directoy;

    public ServerSpawner(IO io, String directory) {
        this.io = io;
        this.directoy = directory;
    }

    @Override
    public void run() {
        String id = UUID.randomUUID().toString();
        try {
            String request = io.read();
            System.out.println("Request for " + id + "\r\n=======\r\n" + request);
            String response = serverResponse(request);
            io.write(response);
            System.out.println("Response for " + id + "\r\n=======\r\n" + response);
        }
        catch (IOException e) {
            System.err.println("Something went wrong communicating with socket.");
            e.printStackTrace();
            io.write(errorResponse());
        }
        catch (Exception e) {
            System.err.println("Something went wrong parsing the request.");
            e.printStackTrace();
            io.write(errorResponse());
        }
        finally {
            try {
                io.close();
            } catch (IOException e) {
                System.err.println("Could not close socket");
                e.printStackTrace();
            }
        }
    }

    private String serverResponse(String request) {
        Server server = new Server(new Parser(request), directoy);
        server.run();
        return new Encoder(server.statusCode, server.headers, server.body).encode();
    }

    private String errorResponse() {
        return new Encoder(500, new HashMap<String, String>(), "Something went wrong").encode();
    }
}
