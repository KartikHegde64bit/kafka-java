package Handler;

import dto.RequestData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class ResponseHandler {
    public void writeResponse(RequestData requestData, OutputStream clientOutputStream) throws IOException {
        ByteArrayOutputStream fullResponse = buildResponse(requestData);

        System.out.println("[writeResponse] Full response bytes: " + Arrays.toString(fullResponse.toByteArray()));
        System.out.println("[writeResponse] Response length: " + fullResponse.size());

        clientOutputStream.write(fullResponse.toByteArray());
    }

    public ByteArrayOutputStream buildResponse(RequestData requestData) throws IOException {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(responseStream);

        // === Response Header ===
        byte[] correlationId = requestData.getCorrelation_id(); // 4 bytes
        out.write(correlationId);  // Correlation ID
        System.out.println("[buildResponse] Correlation ID: " + Arrays.toString(correlationId));

        // === Response Body ===
        out.writeShort(0); // Error code = 0 (2 bytes)
        System.out.println("[buildResponse] Error Code: 0");

        // API versions list
        writeVarInt(out, 1);       // One API version
        System.out.println("[buildResponse] API Versions Count (VarInt): 1");

        out.writeShort(18);        // API key
        out.writeShort(3);         // Min version
        out.writeShort(4);         // Max version
        System.out.println("[buildResponse] API Key: 18, Min Version: 3, Max Version: 4");

        out.writeByte(0);          // Tagged fields for API version
        System.out.println("[buildResponse] Tagged Fields for API Version: 0");

        // Throttle time
        out.writeInt(0);           // 4 bytes throttle time
        System.out.println("[buildResponse] Throttle Time: 0");

        // === Finalize Payload ===
        byte[] payload = responseStream.toByteArray();
        int messageLength = payload.length;
        System.out.println("[buildResponse] Payload bytes: " + Arrays.toString(payload));
        System.out.println("[buildResponse] Message Length: " + messageLength);

        ByteArrayOutputStream fullResponse = new ByteArrayOutputStream();
        DataOutputStream finalOut = new DataOutputStream(fullResponse);

        finalOut.writeInt(messageLength); // 4 bytes message length
        finalOut.write(payload);          // full payload

        System.out.println("[buildResponse] Full Response bytes: " + Arrays.toString(fullResponse.toByteArray()));

        return fullResponse;
    }

    // Kafka-style VarInt encoding
    public static void writeVarInt(DataOutputStream out, int value) throws IOException {
        System.out.print("[writeVarInt] Encoding value: " + value + " as VarInt: ");
        while ((value & 0xFFFFFF80) != 0L) {
            int b = (value & 0x7F) | 0x80;
            out.writeByte(b);
            System.out.print(b + " ");
            value >>>= 7;
        }
        out.writeByte(value & 0x7F);
        System.out.println(value & 0x7F);
    }
}
