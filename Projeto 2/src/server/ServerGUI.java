package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServerGUI extends JFrame {
    private static final long serialVersionUID = 1L;
	private JTextField candidateNumberField;
    private JTextField candidateNameField;
    private JTextArea candidatesArea;
    private JButton addCandidateButton;
    private Map<Integer, String> candidates = new HashMap<>();
    private Map<Integer, Integer> votes = new HashMap<>();

    public ServerGUI() {
        setTitle("Voting Server");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();

        new Thread(this::startServer).start();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        candidateNumberField = new JTextField();
        candidateNameField = new JTextField();
        candidatesArea = new JTextArea();
        candidatesArea.setEditable(false);
        addCandidateButton = new JButton("Add Candidate");

        panel.add(new JLabel("Candidate Number:"));
        panel.add(candidateNumberField);
        panel.add(new JLabel("Candidate Name:"));
        panel.add(candidateNameField);
        panel.add(addCandidateButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(candidatesArea), BorderLayout.CENTER);

        addCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCandidate();
            }
        });
    }

    private void addCandidate() {
        try {
            int candidateNumber = Integer.parseInt(candidateNumberField.getText().trim());
            String candidateName = candidateNameField.getText().trim();

            if (candidateName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a candidate name.");
                return;
            }

            candidates.put(candidateNumber, candidateName);
            votes.put(candidateNumber, 0);
            updateCandidatesArea();
            candidateNumberField.setText("");
            candidateNameField.setText("");
            JOptionPane.showMessageDialog(this, "Candidate added successfully!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid candidate number.");
        }
    }

    private void updateCandidatesArea() {
        StringBuilder sb = new StringBuilder("Candidates and Votes:\n");
        for (Map.Entry<Integer, String> entry : candidates.entrySet()) {
            int candidateNumber = entry.getKey();
            String candidateName = entry.getValue();
            int voteCount = votes.get(candidateNumber);
            sb.append("Number: ").append(candidateNumber)
              .append(", Name: ").append(candidateName)
              .append(", Votes: ").append(voteCount).append("\n");
        }
        candidatesArea.setText(sb.toString());
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            candidatesArea.append("Server started on port 12345...\n");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new ClientHandler(socket).start();
                } catch (IOException e) {
                    candidatesArea.append("Error accepting client connection: " + e.getMessage() + "\n");
                }
            }
        } catch (IOException e) {
            candidatesArea.append("Server error: " + e.getMessage() + "\n");
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                candidatesArea.append("Error setting up input/output streams: " + e.getMessage() + "\n");
            }
        }

        @Override
        public void run() {
            try {
                String request;
                while ((request = input.readLine()) != null) {
                    if (request.startsWith("CONFIRM")) {
                        int candidateNumber = Integer.parseInt(request.split(" ")[1]);
                        if (candidates.containsKey(candidateNumber)) {
                            output.println(candidates.get(candidateNumber));
                        } else {
                            output.println("Candidate not found!");
                        }
                    } else if (request.startsWith("VOTE")) {
                        int candidateNumber = Integer.parseInt(request.split(" ")[1]);
                        if (candidates.containsKey(candidateNumber)) {
                            votes.put(candidateNumber, votes.get(candidateNumber) + 1);
                            output.println("Vote registered for " + candidates.get(candidateNumber));
                            updateCandidatesArea();
                        } else {
                            output.println("Candidate not found!");
                        }
                    } else {
                        output.println("Invalid request!");
                    }
                }
            } catch (IOException e) {
                candidatesArea.append("Error reading from client: " + e.getMessage() + "\n");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    candidatesArea.append("Error closing client socket: " + e.getMessage() + "\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerGUI serverGUI = new ServerGUI();
            serverGUI.setVisible(true);
        });
    }
}
