package Services;

import dto.RequestData;

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

            RequestData requestData = new RequestData(clientInputStream);
            byte[] response = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(requestData.getMessage_size(), 0, response, 0, 4);
            System.arraycopy(requestData.getCorrelation_id(), 0, response, 4, 4);
            response[8] = 0x00;
            response[9] = 0x23;
            clientOutputStream.write(response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
