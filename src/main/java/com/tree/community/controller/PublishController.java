package com.tree.community.controller;

import com.tree.community.cache.TagCache;
import com.tree.community.dto.QuestionDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.model.Question;
import com.tree.community.model.User;
import com.tree.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Long id,Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    @ResponseBody
    public Object doPublish(@RequestBody  Question question, HttpServletRequest request){
        String invalid = TagCache.filterInvalid(question.getTag());
        if(StringUtils.isNoneBlank(invalid)){
            return ResultDTO.errorOf(2016,"输入非法标签:" + invalid);
        }


        User user = (User) request.getSession().getAttribute("user");
        question.setCreator(user.getId());
        question.setId(question.getId());
        questionService.createOrUpdate(question);
        return ResultDTO.okOf();
    }
}
