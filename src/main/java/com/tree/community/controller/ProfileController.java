package com.tree.community.controller;

import com.tree.community.dto.BookMarkDTO;
import com.tree.community.dto.PaginationDTO;
import com.tree.community.model.Question;
import com.tree.community.model.User;
import com.tree.community.service.BookMarkService;
import com.tree.community.service.CollectionService;
import com.tree.community.service.NotificationService;
import com.tree.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BookMarkService bookMarkService;

    @Autowired
    private CollectionService collectionService;

    @GetMapping("/user/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          HttpServletRequest request, Model model,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "10")Integer size){


        User user = (User) request.getSession().getAttribute("user");

        if("questions".equals(action)){
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("sectionName","帖子管理_树洞社区");
            model.addAttribute("section","questions");
            model.addAttribute("pagination",paginationDTO);
        }else if("message".equals(action)){
            PaginationDTO paginationDTO = notificationService.list(user.getId(),page,size);
            model.addAttribute("sectionName","我的消息_树洞社区");
            model.addAttribute("section","message");
            model.addAttribute("pagination",paginationDTO);
        }else if("bookMark".equals(action)){
            List<BookMarkDTO> bookMarkDTOList = bookMarkService.getUserBookMark(user.getId());
            model.addAttribute("bookMarkDTOList",bookMarkDTOList);
            model.addAttribute("sectionName","收藏管理_树洞社区");
            model.addAttribute("section","bookMark");
            PaginationDTO paginationDTO = new PaginationDTO();
            paginationDTO.setData(null);
            model.addAttribute("pagination",paginationDTO);
        }

        return "profile";
    }

    @GetMapping("/user/bookMark/{id}")
    public String userCollection(@PathVariable(name = "id")Long id, Model model,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size) {

        BookMarkDTO bookMarkDTO = bookMarkService.getBookMarkById(id);
        List<Question> questions = collectionService.getCollectionByFolderId(id);
        model.addAttribute("bookMark",bookMarkDTO);
        model.addAttribute("questions",questions);
        model.addAttribute("section","collection");
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setData(null);
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }

}
