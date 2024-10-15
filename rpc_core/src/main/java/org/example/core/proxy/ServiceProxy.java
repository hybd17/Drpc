package org.example.core.proxy;

import cn.hutool.core.collection.CollUtil;
import org.example.core.RpcApplication;
import org.example.core.config.RpcConfig;
import org.example.core.constant.RpcConstant;
import org.example.core.fault.retry.RetryStrategy;
import org.example.core.fault.retry.RetryStrategyFactory;
import org.example.core.fault.tolerant.TolerantStrategy;
import org.example.core.fault.tolerant.TolerantStrategyFactory;
import org.example.core.loadbalancer.LoadBalancer;
import org.example.core.loadbalancer.LoadBalancerFactory;
import org.example.core.model.RpcRequest;
import org.example.core.model.RpcResponse;
import org.example.core.model.ServiceMetaInfo;
import org.example.core.registry.Registry;
import org.example.core.registry.RegistryFactory;
import org.example.core.serializer.Serializer;
import org.example.core.serializer.SerializerFactory;
import org.example.core.server.tcp.VertxTcpClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDK动态代理
 * */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 注册中心获取服务地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)){
                throw new RuntimeException("No service found for " + serviceName);
            }

            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            Map<String,Object> requestParams = new HashMap<>(){{
                put("methodName",rpcRequest.getMethodName());
            }};
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfos);

            //rpc请求
            //重试机制
            RpcResponse rpcResponse;
            try{
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(()->
                        VertxTcpClient.doRequest(rpcRequest,selectedServiceMetaInfo)
                );
            }catch (Exception e){
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                rpcResponse = tolerantStrategy.doTolerant(null,e);
            }
            return rpcResponse.getData();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to call remote service");
        }
    }
}
