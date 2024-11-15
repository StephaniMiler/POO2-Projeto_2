package client;
import java.io.*;
import java.net.*;

public class VotingClient {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public String startConnection(String ip, int port, String vote) throws IOException {
        
    	clientSocket = new Socket(ip, port);

    	out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        try {
        	out.writeObject(vote);
            
        	return (String) in.readObject();
        	
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		return null;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
