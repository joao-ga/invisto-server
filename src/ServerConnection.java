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
                // Criando uma thread p/ cada conexão, para possibilitar conexões paralelas
                new Thread(() -> {
                    try {
                        ClientRequestProcessor processor = new ClientRequestProcessor(clientSocket);
                        processor.processRequest();
                        System.out.println("Thread criada com sucesso!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
