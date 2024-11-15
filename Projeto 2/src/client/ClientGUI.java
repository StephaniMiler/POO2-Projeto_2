package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Voting Client");
            frame.setSize(400, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());

            JLabel label = new JLabel("Enter Candidate Number:");
            JTextField candidateNumberField = new JTextField(10);
            JButton voteButton = new JButton("Vote");

            panel.add(label);
            panel.add(candidateNumberField);
            panel.add(voteButton);

            frame.add(panel);
            frame.setVisible(true);

            voteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String candidateNumberStr = candidateNumberField.getText().trim();
                    if (!candidateNumberStr.isEmpty()) {
                        try {
                            int candidateNumber = Integer.parseInt(candidateNumberStr);
                            processVote(candidateNumber);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid candidate number.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please enter a candidate number.");
                    }
                }
            });
        });
    }

    private static void processVote(int candidateNumber) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            output.println("CONFIRM " + candidateNumber);

            String candidateName = input.readLine();
            if (candidateName.equals("Candidate not found!")) {
                JOptionPane.showMessageDialog(null, "Candidate not found!");
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(
                    null,
                    "You are about to vote for: " + candidateName + "\nDo you confirm your vote?",
                    "Confirm Vote",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                output.println("VOTE " + candidateNumber);
                String response = input.readLine();
                JOptionPane.showMessageDialog(null, response);
            } else {
                JOptionPane.showMessageDialog(null, "Vote cancelled.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the server: " + e.getMessage());
        }
    }
}

