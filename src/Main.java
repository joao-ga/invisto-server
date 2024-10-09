import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8000)) { // A mesma porta usada no Node.js
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Conexão aceita: " + clientSocket.getInetAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String jsonInput = in.readLine();

                    // Usando Gson para deserializar o JSON em um objeto User
                    Gson gson = new Gson();
                    User user = gson.fromJson(jsonInput, User.class);

                    // Validar os dados do usuário
                    String responseMessage;
                    if (validateUser(user)) {
                        responseMessage = "{\"status\":\"success\"}";
                    } else {
                        responseMessage = "{\"status\":\"error\", \"message\":\"Dados inválidos\"}";
                    }

                    // Enviar a resposta de volta para o cliente
                    OutputStream out = clientSocket.getOutputStream();
                    out.write(responseMessage.getBytes());
                    out.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean validateUser(User user) {
        // Implemente sua lógica de validação aqui
        // Por exemplo: verificar se o email é válido, se a senha atende aos requisitos, etc.
        return true; // Retorne true ou false dependendo da validação
    }
}
