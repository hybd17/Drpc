package org.example.core.protocol;

import com.google.protobuf.ProtocolMessageEnum;
import io.vertx.core.buffer.Buffer;
import org.example.core.model.RpcRequest;
import org.example.core.model.RpcResponse;
import org.example.core.serializer.Serializer;
import org.example.core.serializer.SerializerFactory;

import java.io.IOException;

public class ProtocolMessageDecoder {
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException{
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        if(magic!= ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("Invalid magic number");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        ProtocolMessageTypeEnum typeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if(serializerEnum==null){
            throw new RuntimeException("Invalid serializer");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        switch (typeEnum){
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header,rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header,rpcResponse);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("Unsupported message type");
        }
    }
}
