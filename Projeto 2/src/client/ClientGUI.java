package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {

    private static final long serialVersionUID = 1L;
	private JTextField cpfField;
    private JPasswordField senhaField;
    private JTextField votoField;
    private JButton loginButton, registerButton, submitVoteButton, cancelVoteButton;
    private JLabel statusLabel;
    private ClientController Controller;

    public ClientGUI() {
        setTitle("Sistema de Votação - Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(JOptionPane.showConfirmDialog(null, "Are you sure ?") == JOptionPane.OK_OPTION){
                	Controller.endConnection();
                    setVisible(false);
                    dispose();
                    System.exit(NORMAL);
                }
            }
        });
        
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Bem-vindo ao sistema de votação!", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(statusLabel, BorderLayout.NORTH);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        loginPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        loginPanel.add(cpfField);

        loginPanel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        loginPanel.add(senhaField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Registrar");
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        add(loginPanel, BorderLayout.CENTER);

        JPanel votePanel = new JPanel();
        votePanel.setLayout(new GridLayout(2, 2, 10, 10));
        votePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        votoField = new JTextField();
        submitVoteButton = new JButton("Confirmar Voto");
        cancelVoteButton = new JButton("Cancelar");

        votePanel.add(new JLabel("Digite o número do candidato:"));
        votePanel.add(votoField);
        votePanel.add(submitVoteButton);
        votePanel.add(cancelVoteButton);

        votePanel.setVisible(false);
        add(votePanel, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfField.getText();
                String senha = new String(senhaField.getPassword());
                if(Controller.login(cpf, senha)) {
                	votePanel.setVisible(true);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enableRegisterInterface();
                enableLoginInterface();
            }
        });

        submitVoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int voto = Integer.parseInt(votoField.getText());
                Controller.submitVote(voto);
                votePanel.setVisible(false);
                enableLoginInterface();
            }
        });

        cancelVoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Controller.cancelVote();
                votePanel.setVisible(false);
                enableLoginInterface();
            }
        });
    }

    public void setController(ClientController controller) {
    	this.Controller = controller;
    }
    
    public void enableLoginInterface() {
        cpfField.setText("");
        senhaField.setText("");
        statusLabel.setText("");
        setVisible(true);
    }

    public void enableVoteInterface(String message) {
        statusLabel.setText("Você está logado. " + message);
        votoField.setVisible(true);
    }

    public void enableRegisterInterface() {
        String cpf = JOptionPane.showInputDialog(this, "Digite seu CPF");
        if(cpf == null) {
        	return;
        }
        String nome = JOptionPane.showInputDialog(this, "Digite seu nome completo");
        if(nome == null) {
        	return;
        }
        String senha = JOptionPane.showInputDialog(this, "Digite sua senha");
        if(senha == null) {
        	return;
        }
        Controller.register(cpf, nome, senha);
    }
    
    public void disableVoteInterface() {
    	votoField.setVisible(false);
    	submitVoteButton.setVisible(false);
    	cancelVoteButton.setVisible(false);
    }
}
