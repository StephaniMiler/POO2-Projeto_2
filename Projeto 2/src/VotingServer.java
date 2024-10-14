import java.io.*;
import java.net.*;

public class VotingServer {
    private ServerSocket serverSocket;

    public VotingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
    }

    public void start() throws IOException {
        while (true) {
            // Aceitar conexões dos clientes
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Criar streams para enviar e receber dados do cliente
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Enviar e receber dados (exemplo: enviar uma mensagem de boas-vindas)
            out.writeObject("Welcome to the voting system!");

            // Fechar conexão após comunicação
            clientSocket.close();
        }
    }

    public static void main(String[] args) {
        try {
            VotingServer server = new VotingServer(12345); // Porta arbitrária (mude se necessário)
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
