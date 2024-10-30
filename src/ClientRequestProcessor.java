import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientRequestProcessor {
    private Socket clientSocket;

    public ClientRequestProcessor(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void processRequest() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            // Recebe o JSON do cliente
            String jsonInput = in.readLine();
            Gson gson = new Gson();
            User user = gson.fromJson(jsonInput, User.class);

            // Valida os dados do usuário
            String validationMessage = User.validateUser(user);
            String responseMessage = validationMessage(validationMessage);
            // Envia resposta de volta para o cliente

            sendMessage(responseMessage, out);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String validationMessage(String validationMessage) {
        String responseMessage;
        if (validationMessage == null) {
            responseMessage = "{\"status\":\"success\"}";
        } else {
            responseMessage = "{\"status\":\"error\", \"message\":\"" + validationMessage + "\"}";
        }

        return responseMessage;
    }

    public void sendMessage(String responseMessage, OutputStream out) {
        try {
            out.write(responseMessage.getBytes());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
