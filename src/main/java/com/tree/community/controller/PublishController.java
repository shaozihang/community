package com.tree.community.controller;

import com.tree.community.cache.TagCache;
import com.tree.community.dto.QuestionDTO;
import com.tree.community.model.Question;
import com.tree.community.model.User;
import com.tree.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping("/publish")
    public String doPublish(Question question, HttpServletRequest request, Model model){

        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("tags", TagCache.get());
        if(question.getTitle()==null||question.getTitle()==""){
            model.addAttribute("error","标题不能为空！");
            return "publish";
        }
        if(question.getDescription()==null||question.getDescription()==""){
            model.addAttribute("error","问题补充不能为空！");
            return "publish";
        }
        if(question.getTag()==null||question.getTag()==""){
            model.addAttribute("error","标签不能为空！");
            return "publish";
        }

        String invalid = TagCache.filterInvalid(question.getTag());
        if(StringUtils.isNoneBlank(invalid)){
            model.addAttribute("error","输入非法标签:" + invalid);
            return "publish";
        }


        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        question.setCreator(user.getId());
        question.setId(question.getId());
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
