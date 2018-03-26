package com.ofo.service;

import com.ofo.entity.User;

import java.util.List;

public interface IUserService {
    public User getById(int id);

    public List<User> getUser();

    public int insert(User user);

    public int update(User user);
}
