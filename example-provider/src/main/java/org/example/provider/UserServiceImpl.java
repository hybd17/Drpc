package org.example.provider;

import org.example.common.model.User;
import org.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("username "+user.getName());
        return user;
    }
}
