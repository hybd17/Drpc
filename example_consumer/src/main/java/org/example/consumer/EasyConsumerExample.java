package org.example.consumer;

import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.core.config.RpcConfig;
import org.example.core.proxy.ServiceProxyFactory;
import org.example.core.utils.ConfigUtils;

public class EasyConsumerExample {
    //TODO get UserService
    public static void main(String[] args) throws Exception{
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("dy---------------------------------------------------------------");

        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println(newUser.getName());
        }else{
            System.out.println("user not found");
        }
//        long number = userService.getNumber();
//        System.out.println(number);
    }
}
