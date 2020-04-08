package com.tree.community.provider;

import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tree.community.dto.AccessTokenDTO;
import com.tree.community.dto.OauthsUser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QQProvider {

    @Value("${tencentcloud.secretId}")
    private String secretId;

    @Value("${tencentcloud.secretKey}")
    private String secretKey;

    @Value("${captcha.UserIp}")
    private String userIp;

    @Value("${captcha.CaptchaAppId}")
    private Integer captchaAppId;

    @Value("${captcha.AppSecretKey}")
    private String appSecretKey;

    public String getAccessToken(AccessTokenDTO tokenDTO){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id="+tokenDTO.getClient_id()+
                        "&client_secret="+tokenDTO.getClient_secret()+"&redirect_uri="+tokenDTO.getRedirect_uri()+
                        "&code="+tokenDTO.getCode())
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public OauthsUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/me?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            String json = JSONPToJSON(string);
            OauthsUser githubUser = JSON.parseObject(json, OauthsUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return  null;
    }

    public String JSONPToJSON(String jsonp){
        int startIndex = jsonp.indexOf("(");
        int endIndex = jsonp.lastIndexOf(")");
        String json = jsonp.substring(startIndex+1, endIndex);
        return json;
    }

    public Long checkCaptcha(String ticket,String randstr){
        try{
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("captcha.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CaptchaClient client = new CaptchaClient(cred, "", clientProfile);

            String params = "{\"CaptchaType\":9,\"Ticket\":\""+ticket+"\",\"UserIp\":\""+userIp+"\",\"Randstr\":\""+randstr+"\",\"CaptchaAppId\":"+captchaAppId+",\"AppSecretKey\":\""+appSecretKey+"\"}";
            DescribeCaptchaResultRequest req = DescribeCaptchaResultRequest.fromJsonString(params, DescribeCaptchaResultRequest.class);

            DescribeCaptchaResultResponse resp = client.DescribeCaptchaResult(req);

            return resp.getCaptchaCode();
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return -1L;
        }
    }
}
