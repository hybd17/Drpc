package org.example.spring_boot_starter.bootstrap;

import org.example.core.RpcApplication;
import org.example.core.config.RegistryConfig;
import org.example.core.config.RpcConfig;
import org.example.core.model.ServiceMetaInfo;
import org.example.core.registry.LocalRegistry;
import org.example.core.registry.Registry;
import org.example.core.registry.RegistryFactory;
import org.example.spring_boot_starter.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 获取当前bean
 * 判断rpcService注解内容是否存在
 * 存在则反射拿出接口类 拼接ServiceMetaInfo
 * 注册到注册中心
 * 在bean实例化之前使用
 * */

public class RpcProviderBootstrap implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if(rpcService!=null){
            Class<?> interfaceClass = rpcService.interfaceClass();
            if(interfaceClass==void.class){
                interfaceClass = beanClass.getInterfaces()[0];
            }

            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            LocalRegistry.register(serviceName,beanClass);

            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try{
                registry.register(serviceMetaInfo);
            }catch (Exception e){
                throw new RuntimeException(serviceName+" register fail",e);
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean,beanName);
    }
}
