package org.example.core.proxy;


import org.example.core.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 工厂模式创建动态代理对象
 * */
public class ServiceProxyFactory {

    /**
     * 根据服务类获取对象
     * */
    public static <T> T getProxy(Class<T> serviceClass){
        if (RpcApplication.getRpcConfig().isMock()){
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }


    /**
     * 根据服务类获取MOCK对象
     * */
    public static <T> T getMockProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
