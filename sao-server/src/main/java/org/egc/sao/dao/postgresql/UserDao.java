package org.egc.sao.dao.postgresql;

import org.apache.ibatis.annotations.Param;
import org.egc.sao.domain.User;

import java.util.List;

public interface UserDao {
    void insert(User user);
    User findUser(
            @Param(value = "user") User user
    );
    List<User> findAll();
    String findRole(@Param(value = "user") User user);
}
