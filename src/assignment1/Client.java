/**
 * @author Hoang Viet Mai, vietm@student.unimelb.edu.au, 813361.
 * COMP90015 S1 2021, Assignment 1, Multi-threaded Dictionary Server.
 * Client implementation.
 */
package assignment1;

// Dependencies.
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) throws IOException {

        try {
            // Get server address and port from STDIN.
            final String SERVER = args[0];
            final int PORT = Integer.parseInt(args[1]);

            // Open connection to the dictionary server, at port PORT.
            Socket client = new Socket(SERVER, PORT);
            System.out.println("Successfully connected to " + SERVER + " at port " + PORT);

            // Obtaining input and output streams.
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            // Scanner to get input from STDIN.
            Scanner scanner = new Scanner(System.in);

            // Infinite loop to handle communication between client and server's client handler.
            while (true) {
                System.out.println(in.readUTF());

                // Get request from STDIN and send to client handler.
                String request = scanner.nextLine();
                out.writeUTF(request);

                // Terminate connection per client's request and break loop.
                if ( request.equals("{\"operation\": \"exit\"}") ) {
                    System.out.println("Terminating current connection...");
                    client.close();
                    System.out.println("Connection terminated.");
                    break;
                }

                // Get reply from client handler.
                String reply = in.readUTF();
                System.out.println(reply);
            }
            // Close resources.
            scanner.close();
            in.close();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

