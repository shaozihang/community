package com.tree.community.controller;

import com.tree.community.dto.PaginationDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.dto.UserDTO;
import com.tree.community.model.Area;
import com.tree.community.model.City;
import com.tree.community.model.Province;
import com.tree.community.model.User;
import com.tree.community.provider.EmailProvider;
import com.tree.community.service.FollowAndFansService;
import com.tree.community.service.QuestionService;
import com.tree.community.service.UserService;
import com.tree.community.service.UseroauthsService;
import com.tree.community.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
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

    @Autowired
    private UseroauthsService useroauthsService;

    @Autowired
    private EmailProvider emailProvider;

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
                          HttpServletRequest request, Model model,
                          @ModelAttribute("bindResult")String bindResult){

        User user = (User) request.getSession().getAttribute("user");

        if("account".equals(action)){
            Map<String, Integer> map = useroauthsService.getOauthsBindStatus(user.getId());
            model.addAttribute("sectionName","账户中心_树洞社区");
            model.addAttribute("section","account");
            model.addAttribute("github",map.get("github"));
            model.addAttribute("qq",map.get("qq"));
            int bindResult2 = 1;
            if(bindResult.equals("-10")){
                bindResult2 = -10;
            }else if(bindResult.equals("-11")){
                bindResult2 = -11;
            }else if(bindResult.equals("-2")){
                bindResult2 = -2;
            }
            model.addAttribute("bindResult2",bindResult2);
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

    @ResponseBody
    @RequestMapping(value = "/updatePhone/{type}",method = RequestMethod.POST)
    public Object updatePhone(@RequestBody UserDTO userDTO,@PathVariable(name = "type")Integer type, HttpServletRequest request){
        String updatePhonePhone = (String) request.getSession().getAttribute("updatePhonePhone");
        if(updatePhonePhone == null){
            return ResultDTO.errorOf(2022,"请重新验证手机号");
        }
        User user = (User) request.getSession().getAttribute("user");
        HttpSession session = request.getSession();
        Object userCode = session.getAttribute("userCode"+type);
        Object userPhone = session.getAttribute("userPhone"+type);
        if(userCode == null){
            return ResultDTO.errorOf(2011,"验证码已过期，请重新发送");
        }
        if(!userDTO.getCode().equals(String.valueOf(userCode))){
            return ResultDTO.errorOf(2012,"验证码错误");
        }
        if(!userDTO.getPhone().equals(String.valueOf(userPhone))){
            return ResultDTO.errorOf(2013,"手机号与验证码不匹配");
        }
        userService.updatePhone(userDTO.getPhone(),user);
        userService.flushUser(user.getId(),request);
        session.removeAttribute("userCode"+type);
        session.removeAttribute("userPhone"+type);
        session.removeAttribute("updatePhonePhone");
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/getEmailCode",method = RequestMethod.POST)
    public Object getEmailCode(@RequestBody Map<String,String> map, HttpServletRequest request) {
        List<User> result = userService.checkEmail(map.get("email"));
        if(result.size() !=0){
            return ResultDTO.errorOf(2022,"该邮箱已存在，请换一个");
        }
        try {
            emailProvider.sendEmail(map.get("email"),request);
            return ResultDTO.okOf();
        }catch (Exception e){
            e.printStackTrace();
            return ResultDTO.errorOf(2023,"邮件发送失败！请重试");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/bindEmail",method = RequestMethod.POST)
    public Object bindEmail(@RequestBody Map<String,String> map, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object bindEmailPhone = session.getAttribute("bindEmailPhone");
        if(bindEmailPhone == null){
            return ResultDTO.errorOf(2024,"请重新验证手机号！");
        }
        Object sendEmailTime = session.getAttribute("sendEmailTime");
        if(sendEmailTime == null){
            return ResultDTO.errorOf(2025,"请发送验证码！");
        }
        Object sendEmail = session.getAttribute("sendEmail");
        Object sendEmailCode = session.getAttribute("sendEmailCode");
        Date date = new Date();
        if(date.getTime() - Long.valueOf(String.valueOf(sendEmailTime)) > 1000*60*5){
            session.removeAttribute("sendEmailTime");
            return ResultDTO.errorOf(2026,"验证码已过期！");
        }else if(!map.get("emailCode").equals(String.valueOf(sendEmailCode))){
            return ResultDTO.errorOf(2027,"验证码错误！");
        }else if(!map.get("email").equals(String.valueOf(sendEmail))){
            return ResultDTO.errorOf(2028,"邮箱与验证码不匹配");
        }else{
            User user = (User) session.getAttribute("user");
            userService.bindEmail(map.get("email"),user.getId());
            userService.flushUser(user.getId(),request);
        }
        session.removeAttribute("sendEmailTime");
        session.removeAttribute("sendEmail");
        session.removeAttribute("sendEmailCode");
        session.removeAttribute("bindEmailPhone");
        return ResultDTO.okOf();
    }
}
