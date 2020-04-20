package com.tree.community.controller;

import com.tree.community.cache.HotTagCache;
import com.tree.community.dto.PaginationDTO;
import com.tree.community.dto.QuestionDTO;
import com.tree.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "15")Integer size,
                        @RequestParam(name = "search",required = false)String search,
                        @RequestParam(name = "tag",required = false)String tag,
                        @RequestParam(name = "type",required = false)String type,
                        @RequestParam(name = "sort",required = false)String sort){
        List<QuestionDTO> questionTop = questionService.questionTop();
        List<Long> quIds = new ArrayList<>();
        for (QuestionDTO question : questionTop) {
            quIds.add(question.getId());
        }
        PaginationDTO pagination = questionService.list(search,tag,page,size,type,sort,quIds);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination",pagination);
        model.addAttribute("questionTop",questionTop);
        model.addAttribute("search",search);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",tags);
        model.addAttribute("type",type);
        model.addAttribute("sort",sort);
        return "index";
    }
}
