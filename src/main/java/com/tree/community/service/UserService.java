package com.tree.community.service;

import com.tree.community.mapper.UserMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            //创建
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setName(user.getName());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }

    }
}
