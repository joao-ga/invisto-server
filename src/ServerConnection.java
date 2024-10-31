import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection {
    private final int PORT;

    public ServerConnection(int port) {
        this.PORT = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexão aceita: " + clientSocket.getInetAddress());

                // Delegar o processamento do cliente para o ClientRequestProcessor
                ClientRequestProcessor processor = new ClientRequestProcessor(clientSocket);
                processor.processRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
