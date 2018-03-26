package com.ofo.dao;

import com.ofo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> getUser(){
        String sql = "select * from user where 1=1";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper(User.class));
    }

    public User getById(int id) {
        String sql = "select * from user where id = "+id;
        return (User) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(User.class));
    }

    public int insert(User user) {
        String sql = "insert into user(name,time) values(?,?)";
        return jdbcTemplate.update(sql,new Object[]{user.getName(),user.getTime()});
    }

    public int update(User user) {
        String sql = "update user set name = ?,time = ? where id = ?";
        return jdbcTemplate.update(sql,new Object[]{user.getName(),user.getTime(),user.getId()});
    }
}
