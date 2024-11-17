package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import comms.Candidate;
import comms.LoginRequest;
import comms.LoginResponse;
import comms.RegisterRequest;
import comms.RegisterResponse;
import comms.VoteRequest;
import comms.VoteResponse;

public class ClientController {

    private static Socket socket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private static ClientGUI clientGUI;
    
    private LoginRequest login = null;
    
    public void setClientGUI(ClientGUI gui) {
        clientGUI = gui;
    }

    // Conectar ao servidor
    public void connectToServer(String host, int port) {
       
            try {
            	socket = new Socket(host, port);
            	outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    public void endConnection() {
    	try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // Login no sistema
    public boolean login(String cpf, String senha) {
        try {
        	outputStream.writeObject("LOGIN");
        	
            LoginRequest loginRequest = new LoginRequest(cpf, senha);
            outputStream.writeObject(loginRequest);

            LoginResponse response = (LoginResponse) inputStream.readObject();

            if (response.isSuccessful()) {
                clientGUI.enableVoteInterface(response.getMessage());
                this.login = loginRequest;
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Erro no login: " + response.getMessage());
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro na comunicação com o servidor: " + e.getMessage());
        }
		return false;
    }

    // Registrar um novo usuário
    public void register(String cpf, String nome, String senha) {
        try {
        	
        	outputStream.writeObject("REGISTER");

        	
            RegisterRequest registerRequest = new RegisterRequest(cpf, nome, senha);
            outputStream.writeObject(registerRequest);

            RegisterResponse response = (RegisterResponse) inputStream.readObject();

            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");
                clientGUI.enableLoginInterface();
            } else {
                JOptionPane.showMessageDialog(null, "Erro no cadastro: " + response.getMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    // Enviar voto para o servidor
    public void submitVote(int vote) {
        try {
        	
        	if(this.login == null) {
        		JOptionPane.showMessageDialog(null, "Login não realizado!");
        		return;
        	}
        	
        	Candidate candidate = getCandidate(vote);
        	if(JOptionPane.showConfirmDialog(null, "Vote for: " + candidate.getName()) != JOptionPane.OK_OPTION) {
        		JOptionPane.showMessageDialog(null, "Insira o candidato que quer votar novamente!");
        		return;
        	}

        	outputStream.writeObject("VOTE");
        	        	
            VoteRequest voteRequest = new VoteRequest(vote, this.login.getCpf());
            outputStream.writeObject(voteRequest);

            VoteResponse response = (VoteResponse) inputStream.readObject();

            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(null, "Voto registrado com sucesso!");
                clientGUI.disableVoteInterface();
                clientGUI.enableLoginInterface();
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao registrar voto: " + response.getMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
    
    public Candidate getCandidate(int vote){
    		try {
				outputStream.writeObject("GET_CANDIDATE");
				
				outputStream.writeObject(new Candidate(null, vote));
				Candidate candidate = (Candidate) inputStream.readObject();
				
				return candidate;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    		return null;
    };

    // Cancelar voto
    public void cancelVote() {
        clientGUI.enableLoginInterface();
    }
}
	
