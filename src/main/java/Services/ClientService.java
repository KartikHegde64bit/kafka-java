package Services;

import Handler.ResponseHandler;
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

            ResponseHandler responseHandler = new ResponseHandler();

            responseHandler.writeResponse(requestData, clientOutputStream);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
