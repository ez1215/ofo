package com.ofo.controller;

import com.ofo.entity.User;
import com.ofo.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @RequestMapping(value = {"/get"},method = RequestMethod.GET)
    public Object get(Integer id){
        if(id == null)
            return userService.getUser();
        return userService.getById(id);
    }

    @RequestMapping(value = {"/insert"},method = RequestMethod.POST)
    public String insert(User user){
        int count = userService.insert(user);
        return  count > 0 ? "新增成功" : "新增失败";
    }

    @RequestMapping(value = {"/update"},method = RequestMethod.POST)
    public String update(User user) {
        log.info(user.toString());
        int count = userService.update(user);
        return count > 0 ? "修改成功" : "修改失败";
    }
}
