package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientGUI extends JFrame {
	
    private static final long serialVersionUID = 1L;
	private JTextField candidateNumberField;
    private JTextArea messageArea;
    private JButton voteButton;
    private String candidate;

    public ClientGUI() {
        setTitle("Voting System");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        candidateNumberField = new JTextField();
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        voteButton = new JButton("Vote");

        panel.add(new JLabel("Enter candidate number:"));
        panel.add(candidateNumberField);
        panel.add(voteButton);

        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendVote();
            }
        });

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);
    }

    private void sendVote() {
        String candidateNumber = candidateNumberField.getText();

        if (candidateNumber.isEmpty()) {
            messageArea.append("Please enter a candidate number.\n");
            return;
        }
        try {
            VotingClient vote = new VotingClient();
            this.candidate = vote.startConnection("127.0.0.1", 12345, candidateNumber);
            if(this.candidate == null) {
            	this.candidate = "Inexiste";
            }
            this.confirmVote();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    private void confirmVote() {
    	
    }

    public static void main(String[] args) {
    	try {
    		SwingUtilities.invokeLater(() -> {
	            ClientGUI clientGUI = new ClientGUI();
	            clientGUI.setVisible(true);
    		});
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        
    }
}
