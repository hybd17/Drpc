package org.example.core.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        //Http服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();
        server.requestHandler(new HttpServerHandler());
        //监听指定端口
        server.listen(port, result->{
            if (result.succeeded())
                System.out.println("Server is listening port "+ port);
            else
                System.out.println("Failed to start sever "+result.cause());
        });
    }
}
