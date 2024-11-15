package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VotingServer implements Runnable {
	
	private Boolean running = true;
    private ServerSocket serverSocket;

    public VotingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
    }

    @Override
    public void run() {
        while (running) {
            try {
            	// Aceitar conex�es dos clientes
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                //Criar nova objeto Runnable para envio de dados para cliente e liberar listening
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                
                out.writeObject("Welcome to the voting system!");
                System.out.println("Client: " + (String) in.readObject());
                // Fechar conex�o ap�s comunica��o
                clientSocket.close();
            } catch(IOException | ClassNotFoundException e) {
            	e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            VotingServer server = new VotingServer(12345); // Porta arbitr�ria (mudar se necess�rio, coloquei localhost)
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
