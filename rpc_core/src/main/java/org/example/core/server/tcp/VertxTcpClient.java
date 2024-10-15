package org.example.core.server.tcp;

import cn.hutool.core.util.IdUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.example.core.RpcApplication;
import org.example.core.model.RpcRequest;
import org.example.core.model.RpcResponse;
import org.example.core.model.ServiceMetaInfo;
import org.example.core.protocol.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class VertxTcpClient {

    public static RpcResponse doRequest(RpcRequest request, ServiceMetaInfo serviceMetaInfo) throws InterruptedException, ExecutionException {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(),serviceMetaInfo.getServiceHost(), result->{
            if(!result.succeeded()){
                System.err.println("Failed to connect to TCP server");
                return;
            }
            NetSocket socket = result.result();
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(request);
            try{
                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encodeBuffer);
            }catch (IOException e){
                throw new RuntimeException("Failed to encode protocol message");
            }
            //receive response
            TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(
                    buffer -> {
                        try {
                            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>)ProtocolMessageDecoder.decode(buffer);
                            responseFuture.complete(rpcResponseProtocolMessage.getBody());
                        }catch (IOException e){
                            throw new RuntimeException("Failed to decode protocol message");
                        }
                    }
            );
            socket.handler(bufferHandlerWrapper);
        });
        RpcResponse rpcResponse = responseFuture.get();
        netClient.close();
        return rpcResponse;
    }


    public void doStart(){
        Vertx vertx = Vertx.vertx();
        //TODO 硬编码
        vertx.createNetClient().connect(8088,"localhost",res ->{
            if(res.succeeded()){
                System.out.println("connected to TCP server");
                NetSocket socket = res.result();
                for(int i = 0;i<1000;i++){
//                    socket.write("Hello, server!Hello, server!Hello, server!Hello, server!");
                    Buffer buffer = Buffer.buffer();
                    String str = "Hello, server!Hello, server!Hello, server!Hello, server!";
                    buffer.appendInt(0);
                    buffer.appendInt(str.getBytes().length);
                    buffer.appendBytes(str.getBytes());
                    socket.write(buffer);
                }
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            }else{
                System.err.println("TCP connect failed");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().doStart();
    }
}
