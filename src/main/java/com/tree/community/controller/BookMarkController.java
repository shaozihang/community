package com.tree.community.controller;

import com.tree.community.dto.BookMarkDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.model.BookMark;
import com.tree.community.model.User;
import com.tree.community.service.BookMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class BookMarkController {

    @Autowired
    private BookMarkService bookMarkService;

    @ResponseBody
    @RequestMapping(value = "/bookMark/getBookMark",method = RequestMethod.POST)
    public Object getBookMark(@RequestBody Map<String,String> map, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        ResultDTO result;
        if(user == null){
            result = ResultDTO.errorOf(2000,"未登录不能进行收藏，请先登录");
        }else{
            List<BookMarkDTO> bookMark;
            if(map.get("collectionStatus").equals("0")){
                bookMark = bookMarkService.getBookMark(user.getId());
            }else{
                bookMark = bookMarkService.getBookMarkTwo(Long.valueOf(map.get("questionId")),user.getId());
            }
            result = ResultDTO.okOf(bookMark);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/bookMark/addBookMark",method = RequestMethod.POST)
    public Object addBookMark(@RequestBody BookMark bookMark, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        int result = bookMarkService.addBookMark(user.getId(), bookMark);
        if(result == -1){
            return ResultDTO.errorOf(2029,"已经有同名收藏夹");
        }
        Long bookMarkId = bookMarkService.getBookMarkId(user.getId(), bookMark.getName());
        return ResultDTO.okOf(bookMarkId);
    }

    @ResponseBody
    @RequestMapping(value = "/bookMark/updateBookMark",method = RequestMethod.POST)
    public Object updateBookMark(@RequestBody BookMark bookMark, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        int result = bookMarkService.updateBookMark(user.getId(), bookMark);
        if(result == -1){
            return ResultDTO.errorOf(2029,"已经有同名收藏夹");
        }
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/bookMark/deleteBookMark",method = RequestMethod.POST)
    public Object deleteBookMark(@RequestBody Long id){
        bookMarkService.deleteBookMark(id);
        return ResultDTO.okOf();
    }
}
