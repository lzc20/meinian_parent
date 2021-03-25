package com.company.dao;

import com.company.pojo.Permission;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PermissionDao {
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
