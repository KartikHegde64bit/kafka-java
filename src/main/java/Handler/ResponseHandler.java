package Handler;

import dto.RequestData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {
    public void writeResponse(RequestData requestData, OutputStream clientOutputStream) throws IOException {

        ByteArrayOutputStream fullResponse = buildResponse(requestData, clientOutputStream);

        // Send response
        clientOutputStream.write(fullResponse.toByteArray());
    }

    public ByteArrayOutputStream buildResponse(RequestData requestData, OutputStream clientOutputStream) throws IOException {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(responseStream);

        // === Response Header ===
        byte[] correlationId = requestData.getCorrelation_id(); // 4 bytes
        out.write(correlationId);

        // === Response Body ===
        out.writeShort(0); // Error code = 0 (No Error)

        // Number of APIs supported
        out.writeInt(1);

        // API key = 18 (API_VERSIONS)
        out.writeShort(18); // API key
        out.writeShort(0);  // Min version
        out.writeShort(4);  // Max version (must be >= 4)

        // Finalize payload and prepend message length
        byte[] payload = responseStream.toByteArray();
        int messageLength = payload.length;

        ByteArrayOutputStream fullResponse = new ByteArrayOutputStream();
        DataOutputStream finalOut = new DataOutputStream(fullResponse);

        finalOut.writeInt(messageLength); // First 4 bytes: message length
        finalOut.write(payload);
        return fullResponse;
    }



}
