package Handler;

import dto.RequestData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {
    public void writeResponse(RequestData requestData, OutputStream clientOutputStream) throws IOException {

        ByteArrayOutputStream fullResponse = buildResponse(requestData);

        // Send response
        clientOutputStream.write(fullResponse.toByteArray());
    }

    public ByteArrayOutputStream buildResponse(RequestData requestData) throws IOException {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(responseStream);

        // === Response Header ===
        byte[] correlationId = requestData.getCorrelation_id(); // 4 bytes
        out.write(correlationId);

        // === Response Body ===
        out.writeShort(0); // Error code = 0 (No Error)

        // ✅ FIX: Write 1 (API count) as VarInt
        writeVarInt(out, 1);

        // API key = 18 (API_VERSIONS)
        out.writeShort(18); // API key
        out.writeShort(0);  // Min version
        out.writeShort(4);  // Max version

        // Required for flexible versions
        //out.writeByte(0); // Empty tagged fields (VarInt = 0)

        // Finalize payload and prepend message length
        byte[] payload = responseStream.toByteArray();
        int messageLength = payload.length;

        ByteArrayOutputStream fullResponse = new ByteArrayOutputStream();
        DataOutputStream finalOut = new DataOutputStream(fullResponse);

        finalOut.writeInt(messageLength); // First 4 bytes: message length
        finalOut.write(payload);
        return fullResponse;
    }


    public static void writeVarInt(DataOutputStream out, int value) throws IOException {
        while ((value & 0xFFFFFF80) != 0L) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value & 0x7F);
    }



}
