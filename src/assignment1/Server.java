/**
 * @author Hoang Viet Mai, vietm@student.unimelb.edu.au, 813361.
 * COMP90015 S1 2021, Assignment 1, Multi-threaded Dictionary Server.
 * Server implementation.
 */
package assignment1;

// Dependencies.
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.net.*;

public class Server {

    // Allowed port range.
    public static final int PORT_MIN = 1024;
    public static final int PORT_MAX = 65535;

    public static void main(String[] args) throws Exception {

        // Get port from STDIN.
        final int PORT = Integer.parseInt(args[0]);

        // Invalid port, exit program.
        if (PORT < PORT_MIN || PORT > PORT_MAX) {
            System.out.println(String.format("Port must be in range %s-%s. ", PORT_MIN, PORT_MAX ));
            System.exit(-1);
        }

        // Open the Server Socket.
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server started, waiting for connection...");

        // Load the dictionary from source.
        String source = args[1];
        Dictionary dictionary = new Dictionary(source);

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
                Thread thread = new RequestHandler(client, in, out, dictionary, source);

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

    // Error messages.
    private final String INVALID = "Invalid request.";

    // Socket, input stream, output stream.
    final Socket socket;
    final DataInputStream in;
    final DataOutputStream out;
    final String source;

    // Dictionary.
    public Dictionary dictionary;

    // Class constructor.
    public RequestHandler(Socket socket, DataInputStream in, DataOutputStream out, Dictionary dictionary, String source) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.dictionary = dictionary;
        this.source = source;
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
        String requestJSON;
        Request request;

        while(true) {
            String reply;
            try {
                // Receive request (a JSON string) from client and convert it to a Request Object.
                requestJSON = in.readUTF();
                System.out.println(requestJSON);
                request = parseRequest(requestJSON);

                // Empty request.
                if (request == null) {
                    out.writeUTF(INVALID);
                    continue;
                }

                switch (request.operation.toLowerCase()) {

                    // Handle query request.
                    case QUERY:
                        if (request.word == null) {
                            out.writeUTF(INVALID);
                            break;
                        }
                        reply = dictionary.query(request.word);
                        out.writeUTF(reply);
                        break;

                    // Handle add a word request.
                    case ADD:
                        reply = this.dictionary.add(request.word, request.meanings);
                        out.writeUTF(reply);

                        // Append the new word to source dictionary..
                        appendJSON(request.word + ":" + request.meanings);
                        break;

                    // Handle remove a word request.
                    case REMOVE:
                        reply = this.dictionary.remove(request.word);
                        out.writeUTF(reply);

                        // Remove word from source dictionary.
                        dictionary.write(this.source);
                        break;

                    // Handle update a word request.
                    case UPDATE:
                        reply = this.dictionary.update(request.word, request.meanings);
                        out.writeUTF(reply);

                        // Update word in source dictionary.
                        dictionary.write(this.source);
                        break;

                    // Handle connection termination request.
                    case EXIT:
                        // Terminate connection and close the socket per client's request.
                        System.out.println("Terminating connection with " + this.socket + " ...");
                        this.socket.close();
                        System.out.println("Connection with " + socket + " has been terminated per client request.");
                        return;

                    // Handle invalid request.
                    default:
                        out.writeUTF(INVALID);
                        break;
                }
            }
            // Socket error, close thread.
            catch (SocketException e) {
                System.out.println("Socket error: connection with " + socket + " has been terminated.");
                return;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                System.out.println(INVALID);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Append a JSON object to the JSON source file.
    private void appendJSON(String jsonToAppend) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        final String json =  mapper.writeValueAsString(jsonToAppend);
        Files.write(new File(this.source).toPath(), Arrays.asList(json), StandardOpenOption.APPEND);
    }
}

/*
** Representation of a given client request.
*/
class Request {
    public String operation;
    public String word;
    public ArrayList<String> meanings;

    // Class constructor.
    public Request(String operation, String word, ArrayList<String> meanings) {
        this.operation = operation;
        this.word = word;
        this.meanings = meanings;
    }

    // Default constructor.
    public Request() {
    }
}