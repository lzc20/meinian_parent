package com.company.dao;

import com.company.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    User findUserByUsername(String username);
}
