package org.example.common.service;

import org.example.common.model.User;

public interface UserService {
    User getUser(User user);

    /**
     * 测试MOCK
     * */
    default short getNumber() {
        return 1;
    }
}
