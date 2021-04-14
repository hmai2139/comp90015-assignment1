/*
** Dictionary client.
 */
package assignment1;

// Dependencies.
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) throws IOException {
        // Get server and port from STDIN.
        final String SERVER = args[0];
        final int PORT = Integer.parseInt(args[1]);

        // Open connection to the dictionary server, at port PORT.
        Socket socket = new Socket(SERVER, PORT);
        System.out.println("Connected to " + SERVER + " at port " + PORT);

        // Obtaining input and output streams from the socket.
        // Create I/O streams for communicating to the client.
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        System.out.println(in.readUTF());

        // All done, close connection and exit.
        in.close();
        out.close();
        socket.close();
    }
}
