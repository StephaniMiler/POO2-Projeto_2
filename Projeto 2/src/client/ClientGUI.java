package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientGUI extends JFrame {

    private static final long serialVersionUID = 1L;
	private JTextField cpfField;
    private JPasswordField passwordField;
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
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

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
                String senha = new String(passwordField.getPassword());
                if(Controller.login(cpf, senha)) {
                	votePanel.setVisible(true);
                	enableVoteInterface("Vote no candidato da sua escolha.");
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
        passwordField.setText("");
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
        String name = JOptionPane.showInputDialog(this, "Digite seu nome completo");
        if(name == null) {
        	return;
        }
        JPasswordField newPassword = new JPasswordField();
        int confirmation = JOptionPane.showConfirmDialog(this, newPassword, "Digite sua senha", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(confirmation == JOptionPane.OK_OPTION) {
        	String password = new String(newPassword.getPassword());
        	if(password == "" || name == "" || cpf == "") {
            	JOptionPane.showMessageDialog(null, "Preencha corretamente todos os campos!");
            	return;
        	}
            Controller.register(cpf, name, password);
        } else {
        	JOptionPane.showMessageDialog(null, "Cancelado!");
        }
    }
    
    public void waitingConnection() {
    	cpfField.setVisible(false);
    	passwordField.setVisible(false);
    	loginButton.setVisible(false);
    	registerButton.setVisible(false);
    	statusLabel.setText("Esperando conexão com servidor, aguarde!");
    }
    
    public void connected() {
    	cpfField.setVisible(true);
    	passwordField.setVisible(true);
    	loginButton.setVisible(true);
    	registerButton.setVisible(true);
    	statusLabel.setText("Conectado! Bem-vindo ao sistema de votação!");
    }
}
