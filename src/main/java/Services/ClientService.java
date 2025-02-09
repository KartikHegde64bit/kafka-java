package Services;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientService {
    public void handleClientRequest(Socket clientSocket) {
        // first 4 bytes -> message size
        // next 2 bytes -> api version
        // next 2 bytes -> some other version
        // next 4 bytes -> correlation id
        try {
            InputStream clientInputStream = clientSocket.getInputStream();
            OutputStream clientOutputStream = clientSocket.getOutputStream();
            byte[] buffer = new byte[1024];
            byte[] message_size = new byte[4];
            byte[] response = {0, 0, 0, 0, 0, 0, 0, 0};
            if(clientInputStream.read(buffer) != -1) {
                System.arraycopy(buffer, 0, message_size, 0, 4);
                System.arraycopy(buffer, 8, response, 4, 4);
                clientOutputStream.write(response);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
