package com.tree.community.service;

import com.tree.community.mapper.UserMapper;
import com.tree.community.mapper.UseroauthsMapper;
import com.tree.community.model.User;
import com.tree.community.model.Useroauths;
import com.tree.community.model.UseroauthsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UseroauthsService {

    @Autowired
    private UseroauthsMapper useroauthsMapper;

    @Autowired
    private UserMapper userMapper;


    public User findByAccountId(String accountId) {
        UseroauthsExample useroauthsExample = new UseroauthsExample();
        useroauthsExample.createCriteria()
                .andAccountIdEqualTo(accountId);
        List<Useroauths> useroauths = useroauthsMapper.selectByExample(useroauthsExample);
        if(useroauths.size() != 0){//查得到就代表不是第一次登录
            User user = userMapper.selectByPrimaryKey(useroauths.get(0).getUid());
            return user;
        }else {
            return null;
        }
    }
}
