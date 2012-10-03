package org.ekoslow.httpserver;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: ekoslow
 * Date: 9/18/12
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class SocketIO implements IO {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public SocketIO(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public String read() throws IOException {
        String toOutput = "";
        toOutput += readBody(readHeaders(readInitialLine()));
        return toOutput;
    }


    private String readInitialLine() throws IOException {
        return in.readLine() + "\r\n";
    }

    private String readHeaders(String s) throws IOException {
        String line = in.readLine();

        while(hasContent(line)) {
           s += line + "\r\n";
           line = in.readLine();
        }
        return s + "\r\n";
    }

    private boolean hasContent(String line) {
        return line != null && !line.equals("");
    }

    private String readBody(String s) throws IOException {
        int length = 0;
        try {
            length = Integer.parseInt(new Parser(s).headers().get("content-length"));
        } catch(NumberFormatException e) {}
        char[] chars = new char[length];
        in.read(chars, 0, length);
        return s + new String(chars);
    }

    @Override
    public void write(String input) {
        out.print(input);
        out.flush();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
