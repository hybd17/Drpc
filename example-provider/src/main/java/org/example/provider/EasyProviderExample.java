package org.example.provider;

import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.core.RpcApplication;
import org.example.core.config.RegistryConfig;
import org.example.core.config.RpcConfig;
import org.example.core.model.ServiceMetaInfo;
import org.example.core.registry.LocalRegistry;
import org.example.core.registry.Registry;
import org.example.core.registry.RegistryFactory;
import org.example.core.server.HttpServer;
import org.example.core.server.VertxHttpServer;
import org.example.core.server.tcp.VertxTcpServer;

public class EasyProviderExample {
    //TODO provider
    public static void main(String[] args) {
        //初始化
        RpcApplication.init();

        //registry 反射
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        String serviceName = UserService.class.getName();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //start web
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(8080);
    }
}
