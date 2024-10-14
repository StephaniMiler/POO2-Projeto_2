import java.io.*;
import java.net.*;

public class VotingClient {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // O método não precisa lançar ClassNotFoundException, só IOException
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

        // Criar streams para comunicação
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        try {
            // Receber mensagem de boas-vindas do servidor
            String message = (String) in.readObject();
            System.out.println("Server: " + message);
        } catch (ClassNotFoundException e) {
            // Trata a ClassNotFoundException dentro do bloco, caso seja necessária
            e.printStackTrace();
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) {
        VotingClient client = new VotingClient();
        try {
            client.startConnection("127.0.0.1", 12345); // IP local e porta do servidor
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
