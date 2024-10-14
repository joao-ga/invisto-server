import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(8000)) {
        System.out.println("Servidor aguardando conexões...");

        while (true) {
            //conexão com a api estabelecida
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Conexão aceita: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String jsonInput = in.readLine();

                Gson gson = new Gson();
                User user = gson.fromJson(jsonInput, User.class);

                // valida os dados do usuário
                String validationMessage = User.validateUser(user);
                String responseMessage;

                if (validationMessage == null) {
                    responseMessage = "{\"status\":\"success\"}";
                } else {
                    responseMessage = "{\"status\":\"error\", \"message\":\"" + validationMessage + "\"}";
                }

                //envia resposta de volta para o cliente
                OutputStream out = clientSocket.getOutputStream();
                out.write(responseMessage.getBytes());
                out.flush();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

