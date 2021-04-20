/**
 * @author Hoang Viet Mai, vietm@student.unimelb.edu.au, 813361.
 * C
 * OMP90015 S1 2021, Assignment 1, Multi-threaded Dictionary Server.
 * Client implementation.
 */
package assignment1;

// Dependencies.
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONArray;

public class Client {

    // Types of request.
    private static final String QUERY = "query";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String UPDATE = "update";
    private static final String EXIT = "exit";

    // Socket, input and output streams.
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;

    // Client GUI.
    private ClientGUI gui;

    public static void main(String args[]) throws Exception {
        try {
            // Get server address and port from STDIN.
            final String SERVER = args[0];
            final int PORT = Integer.parseInt(args[1]);

            // Open connection to the dictionary server, at port PORT.
            Socket socket = new Socket(SERVER, PORT);
            System.out.println("Successfully connected to " + SERVER + " at port " + PORT);

            // Obtaining input and output streams.
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Scanner to get input from STDIN.
            Scanner scanner = new Scanner(System.in);

            Client client = new Client(socket, in, out, scanner);

            // Infinite loop to handle communication between client and server's client handler.
            while (true) {
                // Get request from STDIN and send to client handler.
                String request = client.scanner.nextLine();
                out.writeUTF(request);

                // Get reply from client handler.
                String reply = in.readUTF();
                System.out.println(reply);
            }
        }

        // Handle connection error.
        catch (ConnectException e) {
            System.out.println("Cannot connect to specified server.");
            System.out.println("The server might be unavailable, or you might have entered invalid server details.");

            // Display error message in GUI.
            ClientGUI.showErrorPanel(
                    "The server might be unavailable, or you might have entered invalid server details.",
                    "Cannot connect to the specified server");
            // Exit.
            System.exit(-1);
        }

        // Handle socket error.
        catch (SocketException e) {
            System.out.println("Socket error: connection to server has been terminated.");
            System.out.println("Please try relaunching the client.");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Class constructor.
    public Client(Socket socket, DataInputStream in, DataOutputStream out, Scanner scanner) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.scanner = scanner;
        initialiseGUI();
    }

    // Initialise GUI.
    public void initialiseGUI() {
        this.gui = new ClientGUI(this);
        gui.getFrame().setVisible(true);
        gui.getFrame().setContentPane(gui.panelMain);
    }

    // Close client.
    private void exit(Socket socket) throws Exception {
        socket.close();
    }

    // Submit query request.
    public String query(String word, DataOutputStream out, DataInputStream in) {
        try {
            String requestJSON = String.format("{ \"operation\": \"%s\", \"word\": \"%s\" }",
                    QUERY, word);
            out.writeUTF(requestJSON);
            String reply = in.readUTF();
            return reply;
        }
        catch (Exception e) {
            String reply = e.getMessage();
            return reply;
        }
    }

    // Submit add request.
    public String add(String word, String meanings, DataOutputStream out, DataInputStream in){
        try {
            JSONArray meaningsJSON = stringToJSONArray(meanings);
            String requestJSON = String.format("{\"operation\": \"%s\", \"word\": \"%s\", \"meanings\": %s }",
                    ADD, word, meaningsJSON);
            out.writeUTF(requestJSON);
            String reply = in.readUTF();
            return reply;
        }
        catch (Exception e) {
            String reply = e.getMessage();
            return reply;
        }
    }

    // Submit remove request.
    public String remove(String word, DataOutputStream out, DataInputStream in) {
        try {
            String requestJSON = String.format("{\"operation\": \"%s\", \"word\": \"%s\" }",
                    REMOVE, word);
            out.writeUTF(requestJSON);
            String reply = in.readUTF();
            return reply;
        }
        catch (Exception e) {
            String reply = e.getMessage();
            return reply;
        }
    }

    // Submit update request.
    public String update(String word, String meanings, DataOutputStream out, DataInputStream in) {
        try {
            JSONArray meaningsJSON = stringToJSONArray(meanings);
            String requestJSON = String.format("{\"operation\": \"%s\", \"word\": \"%s\", \"meanings\": %s }",
                    UPDATE, word, meaningsJSON);
            out.writeUTF(requestJSON);
            String reply = in.readUTF();
            return reply;
        }
        catch (Exception e) {
            String reply = e.getMessage();
            return reply;
        }
    }

    // Convert comma-separated meanings string from client GUI into JSON array.
    public JSONArray stringToJSONArray(String meanings) {
        String[] meaningsArray = meanings.split(",");
        JSONArray meaningsJSON = new JSONArray();

        for (int i = 0; i < meaningsArray.length; i++) {
            meaningsJSON.add(meaningsArray[i]);
        }
        return meaningsJSON;
    }

    public DataOutputStream getOutputStream() {
        return out;
    }

    public DataInputStream getInputStream() {
        return in;
    }
}

