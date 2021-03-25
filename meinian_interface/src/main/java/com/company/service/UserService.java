package com.company.service;

import com.company.pojo.User;

public interface UserService {
    User findUserByUsername(String username);
}
