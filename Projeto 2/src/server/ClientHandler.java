package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import comms.Candidate;
import comms.LoginRequest;
import comms.LoginResponse;
import comms.RegisterRequest;
import comms.RegisterResponse;
import comms.VoteRequest;
import comms.VoteResponse;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerController serverController;
    
    public ClientHandler(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                String action = (String) in.readObject();
                switch (action) {
                    case "LOGIN":
                        handleLogin();
                        break;
                    case "REGISTER":
                        handleRegister();
                        break;
                    case "VOTE":
                        handleVote();
                        break;
                    case "GET_CANDIDATE":
                        handleGetCandidates();
                        break;
                    default:
                    	System.out.println("Hitting here\n");   
                }
            }
        }
        catch (EOFException close) {
        	System.out.println("Connection closed\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogin() throws IOException, ClassNotFoundException {
        LoginRequest loginRequest = (LoginRequest) in.readObject();
        if(serverController.login(loginRequest.getCpf(), loginRequest.getSenha())) {
        	out.writeObject(new LoginResponse(true, "Login com sucesso!"));
        }
        else {
        	out.writeObject(new LoginResponse(false, "Login sem sucesso: CPF ou senha sem cadastro ou erradas"));
        }
    }

    private void handleRegister() throws IOException, ClassNotFoundException {
        RegisterRequest registerRequest = (RegisterRequest) in.readObject();
        if(serverController.registerVoter(registerRequest.getCpf(), registerRequest.getSenha(), registerRequest.getNome())) {
        	out.writeObject(new RegisterResponse(true, "Cadastro realizado com sucesso"));
        }
        else {
        	out.writeObject(new RegisterResponse(false, "CPF já cadastrado"));
        }
    }

    private void handleVote() throws IOException, ClassNotFoundException {
        VoteRequest voteRequest = (VoteRequest) in.readObject();
        if(serverController.vote(voteRequest.getCpf(), voteRequest.getVote())) {
        	out.writeObject(new VoteResponse(true, "Voto computado com sucesso!"));
        }
        else {
        	if(serverController.getVotingOpen()) {
        		out.writeObject(new VoteResponse(false, "CPF já votou!"));
        	} else {
        		out.writeObject(new VoteResponse(false, "Votação encerrada!"));
        	}
        }
    }

    private void handleGetCandidates() throws IOException {
    	Candidate candidate;
		try {
			candidate = (Candidate) in.readObject();
	        out.writeObject(serverController.getCandidate(candidate.getNumber()));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    }
}
