package httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/18/12
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static HashMap<String, String> options = new HashMap<String, String>() {
        {
            put("p", "3000");
            put("d", "public");
        }
    };

    public static void main(String args[]) {
        parseOptions(args);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Integer.parseInt(options.get("p")));
            System.out.println("Listening on port: " + options.get("p"));
            while(true) {
                Socket socket;
                try {
                    socket = serverSocket.accept();
                    (new Thread(new ServerSpawner(new SocketIO(socket), options.get("d")))).start();
                }
                catch (IOException e) {
                    System.err.println("Could not accsept socket");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port 10008");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close socket server");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private static void parseOptions(String[] args) {
        for(int i=0; i < args.length; i+=2) {
            if (args[i].equals("-p")) {
                options.put("p", args[i+1]);
            } else if (args[i].equals("-d")) {
                options.put("d", args[i+1]);
            }
        }
    }
}
