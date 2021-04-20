package assignment1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
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

    // GUI frame.
    private JFrame frame;

    // Dictionary client.
    private Client client;

    // Class constructor.
    public ClientGUI(Client dictionaryClient) {

        this.client = dictionaryClient;

        // Initialise frame.
        frame = new JFrame("Dictionary client");
        frame.setMinimumSize(new Dimension(450, 340));
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    System.exit(-1);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    System.exit(-1);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    System.exit(-1);
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                String response = null;

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
                    System.exit(-1);
                }
            }
        });
    }

    // Get local date and time.
    public String localDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss: ");
        LocalDateTime now = LocalDateTime.now();
        return(dateTimeFormatter.format(now));
    }

    public JFrame getFrame() {
        return frame;
    }

}