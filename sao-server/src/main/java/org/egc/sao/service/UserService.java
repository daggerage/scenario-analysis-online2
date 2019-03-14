package org.egc.sao.service;

import org.egc.sao.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findUser(User user);

    List<User> findAll();

    void insert(User user);

    String findRole(User user);
}
