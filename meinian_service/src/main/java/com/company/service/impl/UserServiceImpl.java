package com.company.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.company.dao.UserDao;
import com.company.pojo.User;
import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User findUserByUsername(String username) {


        User user=userDao.findUserByUsername(username);

        return user;
    }
}
