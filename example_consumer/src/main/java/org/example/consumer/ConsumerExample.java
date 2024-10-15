package org.example.consumer;

import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.core.bootstrap.ConsumerBootstrap;
import org.example.core.config.RpcConfig;
import org.example.core.proxy.ServiceProxyFactory;
import org.example.core.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) throws Exception{
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("dy--------------------------");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
