package com.tree.community.provider;

import com.alibaba.fastjson.JSON;
import com.tree.community.dto.AccessTokenDTO;
import com.tree.community.dto.OauthsUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QQProvider {
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
            OauthsUser githubUser = JSON.parseObject(string, OauthsUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return  null;
    }
}
