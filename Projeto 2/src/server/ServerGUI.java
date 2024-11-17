package server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import comms.Candidate;

public class ServerGUI extends JFrame {
    private static final long serialVersionUID = 1L;
	private JButton addButton, startVoteButton, endVoteButton, generateReportButton;
    private JTextArea logArea;
    private JTextField candidateNameField, candidateNumberField;
    private ServerController serverController;

    public ServerGUI(ServerController serverController) {
        this.serverController = serverController;
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Server");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Candidate Name:"));
        candidateNameField = new JTextField();
        panel.add(candidateNameField);

        panel.add(new JLabel("Candidate Number:"));
        candidateNumberField = new JTextField();
        panel.add(candidateNumberField);

        addButton = new JButton("Add Candidate");
        panel.add(addButton);

        startVoteButton = new JButton("Start Voting");
        endVoteButton = new JButton("End Voting");
        generateReportButton = new JButton("Generate Report");

        panel.add(startVoteButton);
        panel.add(endVoteButton);
        panel.add(generateReportButton);

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                	try {
                    	String name = candidateNameField.getText();
                        int number = Integer.parseInt(candidateNumberField.getText());
                        if(serverController.addCandidate(new Candidate(name, number))) {
                            logAction("Added candidate: " + name + " (" + number + ")");
                        } else {
                        	logAction("Voting already started");
                        }
                    	
                    } catch (NumberFormatException excep) {
                    	logAction("Invalid input, try again.");
                    };
                }
        });

        startVoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serverController.startVoting()) {
                	logAction("Voting has already started!");
                } else {
                	logAction("Voting Started!");
                }
            }
        });

        endVoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serverController.stopVoting()) {;
                	logAction("Voting not started or already ended!");
                }
                else {
                	logAction("Voting ended!");
                }
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serverController.getVotingOpen()) {
                	logAction(serverController.getResults());
                }
                else {
                	logAction("End voteng first!");
                }
            }
        });
    }

    private void logAction(String action) {
        logArea.append(action + "\n");
    }

    
}


