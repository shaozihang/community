package com.tree.community.controller;

import com.tree.community.dto.FileDTO;
import com.tree.community.model.User;
import com.tree.community.provider.AliyunProvider;
import com.tree.community.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FileController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @Autowired
    private AliyunProvider aliyunProvider;

    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDTO upload(HttpServletRequest request){

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            return fileDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/wechat.png");
        return fileDTO;
    }

    @ResponseBody
    @RequestMapping(value = "/file/uploadAvatar/")
    public Map<String,Object> uploadAvatar(@RequestParam("file") MultipartFile file,HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        Map map = new HashMap();
        try {
            if(file == null){
                map.put("code",-1);
            }else {
                String result = aliyunProvider.uploadAvatar(file, user);
                if(result.equals("success")){
                    map.put("code",200);
                }else {
                    map.put("code",0);
                }
            }

        } catch (Exception e) {
            map.put("code",0);
            e.printStackTrace();
        }

        return map;
    }
}
