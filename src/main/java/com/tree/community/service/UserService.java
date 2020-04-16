package com.tree.community.service;

import com.tree.community.dto.ResultDTO;
import com.tree.community.dto.UserDTO;
import com.tree.community.mapper.*;
import com.tree.community.model.*;
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
    private UseroauthsService useroauthsService;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private AreaMapper areaMapper;

    public ResultDTO createOrBind(UserDTO userDTO) {
        User user = new User();
        user.setNickName(userDTO.getNickName());
        user.setPassword(MD5Utils.md5(userDTO.getPassword(),"邵梓航"));
        user.setPhone(userDTO.getPhone());
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtMotified(System.currentTimeMillis());
        user.setAvatarUrl("/images/default-avatar.png");
        userMapper.insertSelective(user);
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andPhoneEqualTo(userDTO.getPhone());
        List<User> users = userMapper.selectByExample(userExample);
        if(StringUtils.isNotBlank(userDTO.getOauthsId())){
            useroauthsService.oauthsBind(users.get(0).getId(),userDTO);
        }
        return ResultDTO.okOf();

    }

    public ResultDTO loginOrBind(UserDTO userDTO,HttpServletRequest request) {
        UserExample userExample = new UserExample();
        if(userDTO.getAccount().contains("@")){
            userExample.createCriteria()
                    .andEmailEqualTo(userDTO.getAccount())
                    .andPasswordEqualTo(MD5Utils.md5(userDTO.getPassword(),"邵梓航"));
        }else{
            userExample.createCriteria()
                    .andPhoneEqualTo(userDTO.getAccount())
                    .andPasswordEqualTo(MD5Utils.md5(userDTO.getPassword(),"邵梓航"));
        }
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            return ResultDTO.errorOf(2015,"账号或密码错误");
        }
        if(StringUtils.isNotBlank(userDTO.getOauthsId())){
            int result = useroauthsService.oauthsBind(users.get(0).getId(), userDTO);
            if(result == -1){
                if(userDTO.getOauthsType().equals("0")){
                    return ResultDTO.errorOf(2018,"Github账号绑定失败！此账号已绑定过其他的Github账号");
                }else if(userDTO.getOauthsType().equals("1")){
                    return ResultDTO.errorOf(2019,"QQ账号绑定失败！此账号已绑定过其他的QQ账号");
                }
            }
        }
        HttpSession session = request.getSession();
        session.setAttribute("user",users.get(0));
        return ResultDTO.okOf();
    }

    public void modifyPwd(Map<String, String> map) {
        User user = new User();
        user.setPassword(MD5Utils.md5(map.get("newPwd"),"邵梓航"));
        UserExample userExample = new UserExample();
        if(map.get("modifyAccount").contains("@")){
            userExample.createCriteria()
                    .andEmailEqualTo(map.get("modifyAccount"));
        }else{
            userExample.createCriteria()
                    .andPhoneEqualTo(map.get("modifyAccount"));
        }
        userMapper.updateByExampleSelective(user, userExample);
    }

    public User getUserInfoById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    public List<Province> getProvinceAll() {
        List<Province> provinces = provinceMapper.selectByExample(new ProvinceExample());
        return provinces;
    }

    public List<City> getCity(Province province) {
        CityExample cityExample = new CityExample();
        if(StringUtils.isNotBlank(province.getName())){
            ProvinceExample example = new ProvinceExample();
            example.createCriteria()
                    .andNameEqualTo(province.getName());
            List<Province> provinces = provinceMapper.selectByExample(example);
            province.setProvinceCode(provinces.get(0).getProvinceCode());
        }
        cityExample.createCriteria()
                .andProvinceCodeEqualTo(province.getProvinceCode());
        List<City> cityList = cityMapper.selectByExample(cityExample);
        return cityList;
    }

    public List<Area> getArea(City city) {
        AreaExample areaExample = new AreaExample();
        if(StringUtils.isNotBlank(city.getName())){
            CityExample example = new CityExample();
            example.createCriteria()
                    .andNameEqualTo(city.getName());
            List<City> cities = cityMapper.selectByExample(example);
            city.setCityCode(cities.get(0).getCityCode());
        }
        areaExample.createCriteria()
                .andCityCodeEqualTo(city.getCityCode());
        List<Area> areaList = areaMapper.selectByExample(areaExample);
        return areaList;
    }

    public void updateUserInfo(User user) {
        if(StringUtils.isBlank(user.getAddress())){
            user.setAddress("保密");
        }
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(user.getId());
        userMapper.updateByExampleSelective(user, example);
    }

    public void updatePhone(String phone, User user) {
        User user1 = new User();
        user1.setPhone(phone);
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(user.getId());
        userMapper.updateByExampleSelective(user1, example);
    }

    public List<User> checkAccount(String account) {
        List<User> users;
        UserExample example = new UserExample();
        if(account.contains("@")){
            example.createCriteria()
                    .andEmailEqualTo(account);
        }else{
            example.createCriteria()
                    .andPhoneEqualTo(account);
        }
        users = userMapper.selectByExample(example);
        return users;
    }

    public List<User> checkEmail(String email) {
        UserExample example = new UserExample();
        example.createCriteria()
                .andEmailEqualTo(email);
        List<User> result = userMapper.selectByExample(example);
        return result;
    }

    public void bindEmail(String email, Long id) {
        User user = new User();
        user.setEmail(email);
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(id);
        userMapper.updateByExampleSelective(user, example);
    }

    public Object adminLogin(User user,HttpServletRequest request) {
        UserExample example = new UserExample();
        example.createCriteria()
                .andPhoneEqualTo(user.getPhone())
                .andPasswordEqualTo(MD5Utils.md5(user.getPassword(),"邵梓航"));
        List<User> users = userMapper.selectByExample(example);
        if(users.size() == 0){
            return ResultDTO.errorOf(2031,"用户名或密码错误");
        }
        if(users.get(0).getType() != 2){
            return ResultDTO.errorOf(2032,"该账号无权访问");
        }
        request.getSession().setAttribute("adminUser",users.get(0));
        return ResultDTO.okOf();
    }
}
