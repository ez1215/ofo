package com.ofo.service.impl;

import com.ofo.dao.UserDao;
import com.ofo.entity.User;
import com.ofo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }

    public List<User> getUser() {
        return userDao.getUser();
    }

    public int insert(User user) {
        if (user == null)
            return  0;
        if (user.getTime() == null)
            user.setTime(new Date());
        return userDao.insert(user);
    }

    public int update(User user) {
        if (user == null || user.getId()==null)
            return  0;
        if (user.getTime() == null)
            user.setTime(new Date());
        return userDao.update(user);
    }
}
