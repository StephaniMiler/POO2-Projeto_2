package client;

public class ClientMain {
    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI();
        ClientController controller = new ClientController();
        controller.setClientGUI(clientGUI);
        clientGUI.setController(controller);
        clientGUI.setVisible(true);

        controller.connectToServer("127.0.0.1", 12345); // Conectar ao servidor
    }
}

