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
        setTitle("Servidor de votação");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Nome do candidato:"));
        candidateNameField = new JTextField();
        panel.add(candidateNameField);

        panel.add(new JLabel("Número do candidato:"));
        candidateNumberField = new JTextField();
        panel.add(candidateNumberField);

        addButton = new JButton("Adicionar candidato");
        panel.add(addButton);

        startVoteButton = new JButton("Começar votação");
        endVoteButton = new JButton("Encerrar votação");
        generateReportButton = new JButton("Gerar relatório");

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
                            logAction("Candidato adicionado: " + name + " (" + number + ")");
                        } else {
                        	if(serverController.getVotingOpen()) {
                        		logAction("Votação já começou");
                        	}
                        	else {
                        		logAction("Candidato já cadastrado!");
                        	}
                        }
                    	
                    } catch (NumberFormatException excep) {
                    	logAction("Entrada inválida, tente novamente!");
                    };
                }
        });

        startVoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serverController.startVoting()) {
                	logAction("Votação já começou!");
                } else {
                	logAction("Votação iniciada!");
                }
            }
        });

        endVoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serverController.stopVoting()) {;
                	logAction("Votação não iniciada ou já terminada!");
                }
                else {
                	logAction("Votação encerrada!");
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
                	logAction("Encerre a votação primeiro!");
                }
            }
        });
    }

    private void logAction(String action) {
        logArea.append(action + "\n");
    }

    
}


