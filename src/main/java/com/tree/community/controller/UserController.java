package com.tree.community.controller;

import com.tree.community.dto.PaginationDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.mapper.CityMapper;
import com.tree.community.mapper.ProvinceMapper;
import com.tree.community.mapper.UserMapper;
import com.tree.community.model.Area;
import com.tree.community.model.City;
import com.tree.community.model.Province;
import com.tree.community.model.User;
import com.tree.community.service.FollowAndFansService;
import com.tree.community.service.QuestionService;
import com.tree.community.service.UserService;
import com.tree.community.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FollowAndFansService followAndFansService;

    @GetMapping(value = "/user/{id}")
    public String user(@PathVariable(name = "id")Long id, Model model,
                       @RequestParam(name = "page",defaultValue = "1")Integer page,
                       @RequestParam(name = "size",defaultValue = "5")Integer size,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        int followStatus;
        int followOtherStatus = 0;
        if(user == null){
            followStatus = 0;
        }else{
            followStatus = followAndFansService.getFollowStatus(user.getId(), id);
            if(followStatus == -1){
                followStatus = 0;
                followOtherStatus = -1;
            }
        }
        User userInfo = userService.getUserInfoById(id);
        PaginationDTO pagination = questionService.list(id, page, size);
        model.addAttribute("userInfo",userInfo);
        model.addAttribute("pagination",pagination);
        model.addAttribute("followStatus",followStatus);
        model.addAttribute("followOtherStatus",followOtherStatus);
        model.addAttribute("section","question");
        model.addAttribute("quCount",pagination.getQuCount());
        return "user";
    }

    @GetMapping("/user/set/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          HttpServletRequest request, Model model){

        User user = (User) request.getSession().getAttribute("user");

        if("account".equals(action)){
            model.addAttribute("sectionName","账户中心_树洞社区");
            model.addAttribute("section","account");
        }else {
            String[] address = new String[]{"请选择省份","请选择城市","请选择地区"};
            List<Province> provinces = userService.getProvinceAll();
            List<City> citys = null;
            List<Area> areas = null;
            if(!user.getAddress().equals("保密")){
                String[] userAddress = user.getAddress().split("-");
                int length = userAddress.length;
                if(length == 1){
                    Province province = new Province();
                    province.setName(userAddress[0]);
                    citys = userService.getCity(province);
                    address[0] = userAddress[0];
                }
                if(length == 2 || length == 3){
                    Province province = new Province();
                    province.setName(userAddress[0]);
                    citys = userService.getCity(province);
                    City city = new City();
                    city.setName(userAddress[1]);
                    areas = userService.getArea(city);
                    address[0] = userAddress[0];address[1] = userAddress[1];
                    if(length == 3){address[2] = userAddress[2];}
                }
            }
            model.addAttribute("sectionName","基本设置_树洞社区");
            model.addAttribute("section","info");
            model.addAttribute("provinces",provinces);
            model.addAttribute("citys",citys);
            model.addAttribute("areas",areas);
            model.addAttribute("myProvince",address[0]);
            model.addAttribute("myCity",address[1]);
            model.addAttribute("myArea",address[2]);
        }

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setData(null);
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }

    @ResponseBody
    @GetMapping(value = "/getCity")
    public Object getCity(String provinceCode){
        Province province = new Province();
        province.setProvinceCode(provinceCode);
        List<City> city = userService.getCity(province);
        return ResultDTO.okOf(city);
    }

    @ResponseBody
    @GetMapping(value = "/getArea")
    public Object getArea(String cityCode){
        City city = new City();
        city.setCityCode(cityCode);
        List<Area> area = userService.getArea(city);
        return ResultDTO.okOf(area);
    }

    @ResponseBody
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    public Object updateUserInfo(@RequestBody User userInfo,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        userInfo.setId(user.getId());
        userService.updateUserInfo(userInfo);
        userService.flushUser(user.getId(),request);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/updatePwd",method = RequestMethod.POST)
    public Object updatePwd(@RequestBody Map<String,String> map, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        String curPwd = MD5Utils.md5(map.get("curPwd"), "邵梓航");
        if(!curPwd.equals(user.getPassword())){
            return ResultDTO.errorOf(2017,"当前密码不正确");
        }
        map.put("modifyPhone",user.getPhone());
        userService.modifyPwd(map);
        userService.flushUser(user.getId(),request);
        return ResultDTO.okOf();
    }
}
