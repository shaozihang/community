package com.tree.community.provider;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.tree.community.mapper.UserMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
public class AliyunProvider {

        @Value("${aliyun.access-key.id}")
        private String accessKeyId;

        @Value("${aliyun.client.secret}")
        private String secret;

        @Value("${aliyun.endpoint}")
        private String endpoint;

        @Value("${aliyun.bucketName}")
        private String bucketName;

        @Value("${aliyun.urlPrefix}")
        private String urlPrefix;

        @Autowired
        private UserMapper userMapper;

    public String SendSms(String phone,Integer type,HttpServletRequest request1) throws ClientException {
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou","Dysmsapi", "dysmsapi.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);

            int userCode = (int) ((Math.random() * 9 + 1) * 100000);
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(phone);
            request.setSignName("树洞社区");
            request.setTemplateCode("SMS_183265936");
            request.setTemplateParam("{\"code\":\""+userCode+"\"}");

            SendSmsResponse response = client.getAcsResponse(request);
            HttpSession session = request1.getSession();
            session.setAttribute("userCode"+type,userCode);
            session.setAttribute("userPhone"+type,phone);
            try {

                    //TimerTask实现5分钟后从session中删除验证码和对应的手机号
                    final Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                    Object userCode = session.getAttribute("userCode"+type);
                                    Object userPhone = session.getAttribute("userPhone"+type);
                                    if(StringUtils.isNotBlank(String.valueOf(userCode)) && StringUtils.isNotBlank(String.valueOf(userPhone))){
                                            //确保清除不出错
                                            session.removeAttribute("userCode"+type);
                                            session.removeAttribute("userPhone"+type);
                                    }
                                    timer.cancel();
                            }
                    },5*60*1000);
            } catch (Exception e) {
                    e.printStackTrace();
            }
            return response.getCode();

    }

    public String uploadAvatar(MultipartFile file, User user){
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, secret);
            String filename = file.getOriginalFilename();
            String filePath = getFilePath(filename);
            // 上传到阿里云
            try {
                    ossClient.putObject(bucketName, filePath, new ByteArrayInputStream(file.getBytes()));
                    User user1 = userMapper.selectByPrimaryKey(user.getId());
                    if(!user1.getAvatarUrl().equals("/images/default-avatar.png")){
                            String avatarUrl = StringUtils.substringAfterLast(user1.getAvatarUrl(), "com:8090/");
                            ossClient.deleteObject(bucketName, avatarUrl);
                    }
            } catch (Exception e) {
                    e.printStackTrace();
                    return "error";
            }
            user.setAvatarUrl( urlPrefix + filePath);
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(user.getId());
            userMapper.updateByExampleSelective(user, example);
            return "success";

    }

    public String getFilePath(String sourceFileName) {
            DateTime dateTime = new DateTime();
            return "images/avatar/" + dateTime.toString("yyyy")
                    + "/" + dateTime.toString("MM") + "/"
                    + dateTime.toString("dd") + "/" + UUID.randomUUID() + "."
                    + StringUtils.substringAfterLast(sourceFileName, ".");
    }



}
