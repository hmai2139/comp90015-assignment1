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
        System.out.println("Server started, waiting for connection...");

        // Infinite loop, awaiting potential requests from clients.

        while (true) {
            Socket client = null;

            try {

                // Initialise socket to receive incoming requests from client.
                client = server.accept();
                System.out.println("A new client is connected : " + client);

                // Create I/O streams to communicate with client.
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                System.out.println("Creating new thread for the current client...");

                // Create a new RequestHandler thread.
                Thread thread = new RequestHandler(client, in, out);

                // Start the thread.
                thread.start();
            }
            catch (Exception e) {
                client.close();
                e.printStackTrace();
            }
        }
    }
}

/*
 ** Handles clients' requests.
 */
class RequestHandler extends Thread {

    // Socket, input stream, output stream.
    final Socket socket;
    final DataInputStream in;
    final DataOutputStream out;

    // Class constructor.
    public RequestHandler(Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    // Overrides default run method.
    @Override
    public void run() {
        String request;
        String reply;

        while(true) {
            try {
                out.writeUTF("Enter \"Exit\" to terminate the current connection.");

                // Ask client for request.
                out.writeUTF("Awaiting your request...");

                // Receive request from client.
                request = in.readUTF();

                if ( request.toLowerCase().equals("exit") ) {
                    // Terminate connection and close the socket per client's request.
                    System.out.println("Terminating current connection...");
                    this.socket.close();
                    System.out.println("Connection terminated.");
                    break;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
