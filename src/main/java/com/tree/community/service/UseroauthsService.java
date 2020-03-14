package com.tree.community.service;

import com.tree.community.dto.UserDTO;
import com.tree.community.mapper.UserMapper;
import com.tree.community.mapper.UseroauthsMapper;
import com.tree.community.model.User;
import com.tree.community.model.Useroauths;
import com.tree.community.model.UseroauthsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UseroauthsService {

    @Autowired
    private UseroauthsMapper useroauthsMapper;

    @Autowired
    private UserMapper userMapper;


    public User findByAccountId(String accountId,Integer type) {
        UseroauthsExample useroauthsExample = new UseroauthsExample();
        useroauthsExample.createCriteria()
                .andAccountIdEqualTo(accountId)
                .andTypeEqualTo(type);
        List<Useroauths> useroauths = useroauthsMapper.selectByExample(useroauthsExample);
        if(useroauths.size() != 0){//查得到就代表不是第一次登录
            User user = userMapper.selectByPrimaryKey(useroauths.get(0).getUid());
            return user;
        }else {
            return null;
        }
    }

    public Map<String, Integer> getOauthsBindStatus(Long id) {
        UseroauthsExample example = new UseroauthsExample();
        example.createCriteria()
                .andUidEqualTo(id);
        List<Useroauths> list = useroauthsMapper.selectByExample(example);
        Map<String, Integer> map = new HashMap<>();
        map.put("github",0);map.put("qq",0);
        if(list.size() != 0){
            for (Useroauths oauth : list) {
                if(oauth.getType() == 0){
                    map.put("github",1);
                }else if (oauth.getType() == 1){
                    map.put("qq",1);
                }
            }
        }
        return map;
    }

    public void oauthsBind(Long uid, UserDTO userDTO){
        Useroauths useroauths = new Useroauths();
        useroauths.setUid(uid);
        useroauths.setAccountId(userDTO.getOauthsId());
        useroauths.setType(Integer.valueOf(userDTO.getOauthsType()));
        useroauthsMapper.insert(useroauths);
    }

    public void unbind(Long id, int type) {
        UseroauthsExample example = new UseroauthsExample();
        example.createCriteria()
                .andUidEqualTo(id)
                .andTypeEqualTo(type);
        useroauthsMapper.deleteByExample(example);
    }
}
