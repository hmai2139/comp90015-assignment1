/*
** Multi-thread dictionary server.
*/
package assignment1;

// Dependencies.
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {

        // Get port from STDIN.
        final int PORT = Integer.parseInt(args[0]);

        // Open the Server Socket.
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server started, waiting for connection...");

        // Load the dictionary from source.
        Dictionary dictionary = new Dictionary(args[1]);

        // Awaiting potential requests from clients.
        while (true) {
            Socket client = null;

            try {

                // Initialise socket to receive incoming requests from client.
                client = server.accept();
                System.out.println("A new client is connected : " + client);

                // Create I/O streams to communicate with client.
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                System.out.println("Creating new thread for the client " + client + "...");

                // Create a new RequestHandler thread.
                Thread thread = new RequestHandler(client, in, out, dictionary);

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

    // Types of request.
    private final String QUERY = "query";
    private final String ADD = "add";
    private final String REMOVE = "remove";
    private final String UPDATE = "update";
    private final String EXIT = "exit";

    // Socket, input stream, output stream.
    final Socket socket;
    final DataInputStream in;
    final DataOutputStream out;

    // Dictionary.
    public Dictionary dictionary;

    // Class constructor.
    public RequestHandler(Socket socket, DataInputStream in, DataOutputStream out, Dictionary dictionary) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.dictionary = dictionary;
    }

    // Take a client request (a JSON string) and convert it to a Request object.
    private Request parseRequest(String requestJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Request request = mapper.readValue(requestJSON, Request.class);
            return request;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Overrides default run method.
    @Override
    public void run() {
        String requestJson;
        Request request;

        while(true) {
            Object reply;
            try {
                out.writeUTF("Awaiting your request...");

                // Receive request (a JSON string) from client and convert it to a Request Object.
                requestJson = in.readUTF();
                request = parseRequest(requestJson);

                // Empty request.
                if (request == null) {
                    out.writeUTF("Invalid request.");
                    continue;
                }

                switch (request.operation.toLowerCase()) {

                    // Handle query request.
                    case QUERY:
                        if ( Dictionary.words.containsKey(request.word.toLowerCase()) ) {
                            ArrayList meanings = (ArrayList) this.dictionary.query(request.word);
                            reply = "";
                            for (Object meaning: meanings) {
                                reply += (String) meaning +"\n";
                            }
                            out.writeUTF((String) reply);
                            break;
                        }
                        reply = "No such word exists";
                        out.writeUTF((String) reply);
                        break;

                    // Handle add a word request.
                    case ADD:
                        reply = this.dictionary.add(request.word, request.meanings);
                        out.writeUTF((String) reply);
                        break;

                    // Handle remove a word request.
                    case REMOVE:
                        reply = this.dictionary.remove(request.word);
                        out.writeUTF((String) reply);
                        break;

                    // Handle update a word request.
                    case UPDATE:
                        reply = this.dictionary.update(request.word, request.meanings);
                        out.writeUTF((String) reply);
                        break;

                    // Handle connection termination request.
                    case EXIT:
                        // Terminate connection and close the socket per client's request.
                        System.out.println("Terminating connection with " + this.socket + " ...");
                        this.socket.close();
                        System.out.println("Connection terminated.");
                        break;

                    default:
                        out.writeUTF("Invalid request.");
                        break;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

/*
** Representation of a given client request.
*/
class Request {
    public String operation;
    public String word;
    public String[] meanings;

    // Class constructor.
    public Request(String operation, String word, String[] meanings) {
        this.operation = operation;
        this.word = word;
        this.meanings = meanings;
    }

    // Default constructor.
    public Request() {
    }
}