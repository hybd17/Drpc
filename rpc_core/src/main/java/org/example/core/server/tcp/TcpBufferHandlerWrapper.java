package org.example.core.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import org.example.core.protocol.ProtocolConstant;
import org.example.core.protocol.ProtocolMessage;

public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler){
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler){
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEAD_LENGTH);
        parser.setOutput(new Handler<Buffer>() {

            int size = -1;
            Buffer result = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if(size==-1){
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    result.appendBuffer(buffer);
                }
                else{
                    result.appendBuffer(buffer);
                    bufferHandler.handle(result);
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEAD_LENGTH);
                    size = -1;
                    result = Buffer.buffer();
                }
            }
        });
        return parser;
    }
}
