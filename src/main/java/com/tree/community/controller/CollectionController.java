package com.tree.community.controller;


import com.tree.community.dto.ResultDTO;
import com.tree.community.model.User;
import com.tree.community.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @ResponseBody
    @RequestMapping(value = "/addCollection",method = RequestMethod.POST)
    public Object addCollection(@RequestBody Map<String,String> map, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        collectionService.addCollection(user.getId(),map);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/updateCollection",method = RequestMethod.POST)
    public Object updateCollection(@RequestBody Map<String,String> map, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(map.get("folderId").equals("取消收藏")){
            collectionService.deleteCollection(user.getId(),map.get("questionId"));
            return ResultDTO.okOf();
        }
        collectionService.updateCollection(user.getId(),map);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/deleteCollection",method = RequestMethod.POST)
    public Object deleteCollection(@RequestBody Map<String,String> map){
        collectionService.delCollection(map);
        return ResultDTO.okOf();
    }

}
