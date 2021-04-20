/**
 * @author Hoang Viet Mai, vietm@student.unimelb.edu.au, 813361.
 * COMP90015 S1 2021, Assignment 1, Multi-threaded Dictionary Server.
 * Client Graphical User Interface (GUI) implementation.
 */
package assignment1;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class ClientGUI {

    // GUI Components.
    public JPanel panelMain;
    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    private JLabel wordFieldLabel;
    private JTextField wordField;
    private JLabel meaningsFieldLabel;
    private JTextField meaningsField;
    private JButton queryButton;
    private JTextArea textLogArea;
    private JScrollPane textLogAreaPane;
    private JPanel buttonPanel;
    private JPanel meaningFieldPanel;
    private JPanel wordFieldPanel;
    private JOptionPane optionPane;

    // Error message components.
    public static JFrame errorFrame;
    public static JPanel errorPanel;

    // GUI frame.
    private final JFrame frame;

    // Dictionary client.
    private final Client client;

    // Class constructor.
    public ClientGUI(Client dictionaryClient) {

        this.client = dictionaryClient;

        // Initialise frame.
        frame = new JFrame("Dictionary client");
        frame.setMinimumSize(new Dimension(450, 340));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        queryButton.addActionListener(e -> {
            String word = wordField.getText();

            try {
                // No words provided.
                if (word == null || word.equals("")) {
                    JOptionPane.showMessageDialog(frame, "Please enter a word.",
                            "Warning", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Perform operation, then clear the text field.
                    String response = client.query(word, client.getOutputStream(), client.getInputStream());
                    textLogArea.append(localDateTime());
                    textLogArea.append(response);
                    textLogArea.append("\n");
                    wordField.setText("");
                }
            }
            catch (Exception exception) {
                textLogArea.append(exception.getMessage());
                textLogArea.append("\n");
            }
        });

        addButton.addActionListener(e -> {
            String word = wordField.getText();
            String meanings = meaningsField.getText();

            try {
                // No words provided.
                if (word == null || word.equals("")) {
                    JOptionPane.showMessageDialog(frame, "Please enter a word.",
                            "Warning", JOptionPane.ERROR_MESSAGE);
                }

                // No meanings provided.
                if (meanings == null || meanings.equals("")) {
                    JOptionPane.showMessageDialog(frame, "Please provide a meaning(s).",
                            "Warning", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Perform operation, then clear the text field.
                    String response = client.add(word, meanings, client.getOutputStream(), client.getInputStream());
                    textLogArea.append(localDateTime());
                    textLogArea.append(response);
                    textLogArea.append("\n");
                    wordField.setText("");
                    meaningsField.setText("");
                }
            }
            catch (Exception exception) {
                textLogArea.append(exception.getMessage());
                textLogArea.append("\n");
            }
        });

        updateButton.addActionListener(e -> {
            String word = wordField.getText();
            String meanings = meaningsField.getText();

            try {
                // No words provided.
                if (word == null || word.equals("")) {
                    JOptionPane.showMessageDialog(frame, "Please enter a word.",
                            "Warning", JOptionPane.ERROR_MESSAGE);
                }

                // No meanings provided.
                if (meanings == null || meanings.equals("")) {
                    JOptionPane.showMessageDialog(frame, "Please provide a meaning(s).",
                            "Warning", JOptionPane.ERROR_MESSAGE);
                }

                else {
                    // Perform operation, then clear the text field.
                    String response = client.update(word, meanings, client.getOutputStream(), client.getInputStream());
                    textLogArea.append(localDateTime());
                    textLogArea.append(response);
                    textLogArea.append("\n");
                    wordField.setText("");
                    meaningsField.setText("");
                }
            }
            catch (Exception exception) {
                textLogArea.append(exception.getMessage());
                textLogArea.append("\n");
            }
        });

        removeButton.addActionListener(e -> {
            String word = wordField.getText();
            String response;

            try {
                // No words provided.
                if (word == null || word.equals("")) {
                    JOptionPane.showMessageDialog(frame, "Please enter a word.",
                            "Warning", JOptionPane.ERROR_MESSAGE);
                }

                else {
                    // Perform operation, then clear the text field.
                    response = client.remove(word, client.getOutputStream(), client.getInputStream());
                    textLogArea.append(localDateTime());
                    textLogArea.append(response);
                    textLogArea.append("\n");
                    wordField.setText("");
                }
            }
            catch (Exception exception) {
                textLogArea.append(exception.getMessage());
                textLogArea.append("\n");
            }
        });
    }

    // Get local date and time.
    public String localDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss: ");
        LocalDateTime now = LocalDateTime.now();
        return(dateTimeFormatter.format(now));
    }

    // Display error message if error is encountered during start-up.
    public static void showErrorPanel(String message, String error) {
        errorFrame = new JFrame("Dictionary client");
        errorFrame.setMinimumSize(new Dimension(450, 340));
        errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        errorFrame.pack();
        errorFrame.setLocationRelativeTo(null);
        JOptionPane.showMessageDialog(errorFrame, message, error, JOptionPane.ERROR_MESSAGE);
    }

    public JFrame getFrame() {
        return frame;
    }

}