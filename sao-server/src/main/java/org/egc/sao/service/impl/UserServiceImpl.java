package org.egc.sao.service.impl;

import org.egc.sao.dao.postgresql.UserDao;
import org.egc.sao.domain.User;
import org.egc.sao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao dao;

    @Autowired
    public UserServiceImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public User findUser(User user) {
        return dao.findUser(user);
    }

    @Override
    public List<User> findAll() {
        return dao.findAll();
    }

    @Override
    public void insert(User user) {
        dao.insert(user);
    }

    @Override
    public String findRole(User user) {
        return dao.findRole(user);
    }
}
