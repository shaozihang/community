package com.tree.community.controller.admin;

import com.tree.community.dto.ResultDTO;
import com.tree.community.model.Question;
import com.tree.community.model.User;
import com.tree.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class AdminController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/admin/{action}")
    public String adminLogin(@PathVariable(name = "action")String action, Model model){
        if("index".equals(action)){
            model.addAttribute("section","index");
        }else if("essence".equals(action)){
            model.addAttribute("section","essence");
        }else if("notice".equals(action)){
            Question question = new Question();
            model.addAttribute("question",question);
            model.addAttribute("section","notice");
        }
        return "admin";
    }

    @ResponseBody
    @GetMapping("/admin/getQuestion")
    public Map<String, Object> getQuestion(@RequestParam(name = "page",defaultValue = "0")Integer page,
                        @RequestParam(name = "limit",defaultValue = "5")Integer limit){
        List<Question> essences = questionService.getQuestion(page-1, limit);
        int count = questionService.getQuestionCount();
        Map<String, Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data",essences);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/admin/questionEdit",method = RequestMethod.POST)
    public Object questionEdit(@RequestBody Question question){
        questionService.questionEdit(question);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/admin/notice",method = RequestMethod.POST)
    public Object notice(@RequestBody Question question, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        question.setCreator(user.getId());
        question.setTag("公告");
        question.setType(7);
        questionService.createOrUpdate(question,request);
        return ResultDTO.okOf();
    }

    @GetMapping("/admin/notice/{id}")
    public String edit(@PathVariable(name = "id")Long id,Model model){
        Question question = questionService.getQuById(id);
        model.addAttribute("question",question);
        model.addAttribute("section","notice");
        return "admin";
    }
}
