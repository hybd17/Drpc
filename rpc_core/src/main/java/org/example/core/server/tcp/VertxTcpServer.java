package org.example.core.server.tcp;

import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import org.example.core.server.HttpServer;
import org.example.core.server.VertxHttpServer;

public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData){
        //TODO 处理请求
        return "hello client".getBytes();
    }
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        NetServer server = vertx.createNetServer();

        //处理请求
        server.connectHandler(new TcpServerHandler());
        //listen on port
        server.listen(port, result -> {
           if(result.succeeded()){
               System.out.println("Tcp server started on port " + port);
           }else{
               System.err.println("Tcp server start failed" + result.cause());
           }
        });
    }


    public static void main(String[] args) {
        new VertxTcpServer().doStart(8088);
    }
}
