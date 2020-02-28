package com.tree.community.service;

import com.tree.community.dto.ResultDTO;
import com.tree.community.dto.UserDTO;
import com.tree.community.mapper.UserMapper;
import com.tree.community.mapper.UserRoleMapper;
import com.tree.community.mapper.UseroauthsMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import com.tree.community.model.UserRole;
import com.tree.community.model.Useroauths;
import com.tree.community.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UseroauthsMapper useroauthsMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        //userExample.createCriteria()
               // .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            //创建
           // user.setGmtCreate(System.currentTimeMillis());
           // user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
           // updateUser.setName(user.getName());
            updateUser.setAvatarUrl(user.getAvatarUrl());
           // updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }

    }

    public ResultDTO createOrBind(UserDTO userDTO) {
        User user = new User();
        user.setNickName(userDTO.getNickName());
        user.setPassword(MD5Utils.md5(userDTO.getPassword(),"邵梓航"));
        user.setPhone(userDTO.getPhone());
        user.setGmtCteate(System.currentTimeMillis());
        user.setGmtMotified(System.currentTimeMillis());
        user.setAvatarUrl("/images/default-avatar.png");
        userMapper.insertSelective(user);
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andPhoneEqualTo(userDTO.getPhone());
        List<User> users = userMapper.selectByExample(userExample);
        UserRole userRole = new UserRole();
        userRole.setUserId(users.get(0).getId());
        userRole.setRoleId(1L);
        userRoleMapper.insert(userRole);
        if(StringUtils.isNotBlank(userDTO.getOauthsId())){
            oauthsBind(users.get(0).getId(),userDTO);
        }
        return ResultDTO.okOf();

    }

    public ResultDTO loginOrBind(UserDTO userDTO,HttpServletRequest request) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andPhoneEqualTo(userDTO.getPhone())
                .andPasswordEqualTo(MD5Utils.md5(userDTO.getPassword(),"邵梓航"));
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            return ResultDTO.errorOf(2015,"账号或密码错误");
        }
        if(StringUtils.isNotBlank(userDTO.getOauthsId())){
            oauthsBind(users.get(0).getId(),userDTO);
        }
        HttpSession session = request.getSession();
        session.setAttribute("user",users.get(0));
        return ResultDTO.okOf();
    }

    public void oauthsBind(Long uid,UserDTO userDTO){
        Useroauths useroauths = new Useroauths();
        useroauths.setUid(uid);
        useroauths.setAccountId(userDTO.getOauthsId());
        useroauths.setType(Integer.valueOf(userDTO.getOauthsType()));
        useroauthsMapper.insert(useroauths);
    }

    public void modifyPwd(Map<String, String> map) {
        User user = new User();
        user.setPassword(MD5Utils.md5(map.get("newPwd"),"邵梓航"));
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andPhoneEqualTo(map.get("modifyPhone"));
        userMapper.updateByExampleSelective(user, userExample);
    }
}
