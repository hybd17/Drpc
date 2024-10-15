package org.example.core.protocol;

import io.vertx.core.buffer.Buffer;
import org.example.core.serializer.Serializer;
import org.example.core.serializer.SerializerFactory;

import java.io.IOException;

public class ProtocolMessageEncoder{
    public static Buffer encode(ProtocolMessage<?> message) throws IOException {
        if(message==null || message.getHeader()==null){
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = message.getHeader();
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum==null){
            throw new RuntimeException("Unsupported serializer");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serialize(message.getBody());
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
        return buffer;
    }
}
