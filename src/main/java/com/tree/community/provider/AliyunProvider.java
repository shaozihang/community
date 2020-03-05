package com.tree.community.provider;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Timer;
import java.util.TimerTask;
@Service
public class AliyunProvider {

        @Value("${aliyun.access-key.id}")
        private String accessKeyId;

        @Value("${aliyun.client.secret}")
        private String secret;

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
}
