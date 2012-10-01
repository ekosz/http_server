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
    public HashMap<String, String> options = new HashMap<String, String>() {
        {
            put("p", "3000");
            put("d", "public");
        }
    };
    public ServerSocket serverSocket;
    private boolean loop;
    private ServerSocketFactory serverSocketFactory;
    private ThreadFactory threadFactory;

    public static void main(String args[]) {
        Main main = new Main(args);
        main.start();
    }

    public Main(String[] args) {
        parseOptions(args);
        this.loop = true;
        this.serverSocketFactory = new ServerSocketFactory();
        this.threadFactory = new ThreadFactory();
    }

    public Main(ServerSocketFactory ss, String[] args) {
        parseOptions(args);
        this.serverSocketFactory = ss;
        this.loop = false;
        this.threadFactory = new ThreadFactory();
    }

    public Main(ThreadFactory threadFactory, String[] args) {
        parseOptions(args);
        this.loop = false;
        this.serverSocketFactory = new ServerSocketFactory();
        this.threadFactory = threadFactory;
    }

    public void start() {
       try {
            setupSocketServer();
            enterListenLoop();
        } catch (IOException e) {
            System.err.println("Could not listen on port " + options.get("p"));
            e.printStackTrace();
        }
    }

    private void enterListenLoop() {
        do {
            try {
                Socket socket = listenForConnection();
                startServerSpawnerThread(socket);
            } catch (IOException e) {
                System.err.println("Could not accept socket");
                e.printStackTrace();
            }
        } while(loop);
    }

    public void startServerSpawnerThread(Socket socket) {
        Thread thread = null;
        try {
            thread = createServerSpawnerThread(socket);
            thread.start();
        } catch (IOException e) {
            System.err.println("Could not start thread.");
            e.printStackTrace();
        }
    }

    private Thread createServerSpawnerThread(Socket socket) throws IOException {
        return threadFactory.generate(new ServerSpawner( new SocketIO(socket), options.get("d") ));
    }

    public Socket listenForConnection() throws IOException {
        return serverSocket.accept();
    }

    public void setupSocketServer() throws IOException {
        serverSocket = serverSocketFactory.generate(Integer.parseInt(options.get("p")));
        System.out.println("Listening on port: " + options.get("p"));
    }

    private void parseOptions(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-p")) {
                options.put("p", args[i + 1]);
            } else if (args[i].equals("-d")) {
                options.put("d", args[i + 1]);
            }
        }
    }
}
