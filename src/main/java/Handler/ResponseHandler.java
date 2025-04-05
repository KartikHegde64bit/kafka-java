package Handler;

import dto.RequestData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {
    public void writeResponse(RequestData requestData, OutputStream clientOutputStream) throws IOException {
        ByteArrayOutputStream fullResponse = buildResponse(requestData);
        clientOutputStream.write(fullResponse.toByteArray());
    }

    public ByteArrayOutputStream buildResponse(RequestData requestData) throws IOException {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(responseStream);

        // === Response Header ===
        byte[] correlationId = requestData.getCorrelation_id(); // 4 bytes
        out.write(correlationId);  // Correlation ID

        // === Response Body ===
        out.writeShort(0); // Error code = 0 (2 bytes)

        writeVarInt(out, 1); // VarInt(1) => compact array size = 1 (1 byte)

        out.writeShort(18); // API key = 18 (2 bytes)
        out.writeShort(3);  // Min version = 3 (2 bytes)
        out.writeShort(4);  // Max version = 4 (2 bytes)

        out.writeByte(0); // Tagged fields (VarInt = 0) (1 byte)

        out.writeInt(0); // Throttle time (4 bytes)

        // === Finalize Payload ===
        byte[] payload = responseStream.toByteArray();
        int messageLength = payload.length;

        ByteArrayOutputStream fullResponse = new ByteArrayOutputStream();
        DataOutputStream finalOut = new DataOutputStream(fullResponse);

        finalOut.writeInt(messageLength); // 4 bytes message length
        finalOut.write(payload);          // full payload

        return fullResponse;
    }

    // Kafka-style VarInt encoding
    public static void writeVarInt(DataOutputStream out, int value) throws IOException {
        while ((value & 0xFFFFFF80) != 0L) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value & 0x7F);
    }
}
