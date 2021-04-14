/*
** Multi-thread dictionary server.
 */
package assignment1;

// Dependencies.
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Server {

    public static void main(String[] args) throws IOException {

        // Get port from STDIN.
        final int PORT = Integer.parseInt(args[0]);

        // Open the Server Socket.
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server started, waiting for request...");
        // Wait for the Client Request.
        Socket client = server.accept();
        System.out.println("A new client is connected : " + client);

        // Create I/O streams for communicating to the client.
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        // Send message to client upon connection.
        out.writeUTF("Greetings from server :D");

        // Close the connection, but not the server socket.
        out.close();
        in.close();
        client.close();
    }
}
