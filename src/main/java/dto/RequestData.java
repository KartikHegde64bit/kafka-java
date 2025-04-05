package dto;

import java.io.IOException;
import java.io.InputStream;

public class RequestData {
    private byte[] message_size = new byte[4];
    private byte[] request_api_key = new byte[2];
    private byte[] request_api_version = new byte[2];
    private byte[] correlation_id = new byte[4];

    public byte[] getRequest_api_key() {
        return request_api_key;
    }

    public void setRequest_api_key(byte[] request_api_key) {
        this.request_api_key = request_api_key;
    }

    public byte[] getCorrelation_id() {
        return correlation_id;
    }

    public void setCorrelation_id(byte[] correlation_id) {
        this.correlation_id = correlation_id;
    }

    public byte[] getRequest_api_version() {
        return request_api_version;
    }

    public void setRequest_api_version(byte[] request_api_version) {
        this.request_api_version = request_api_version;
    }

    public byte[] getMessage_size() {
        return message_size;
    }

    public void setMessage_size(byte[] message_size) {
        this.message_size = message_size;
    }

    public RequestData(InputStream clientInputStream) throws IOException {
        byte[] buffer = new byte[1024];
        if(clientInputStream.read(buffer) != -1) {
            System.arraycopy(buffer, 0, message_size, 0, 4);
            System.arraycopy(buffer, 0, request_api_key, 4, 2);
            System.arraycopy(buffer, 0, request_api_version, 6, 2);
            System.arraycopy(buffer, 4, correlation_id, 8, 4);
        }


    }
}
