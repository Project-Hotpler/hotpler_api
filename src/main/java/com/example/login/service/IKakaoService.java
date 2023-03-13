package com.example.login.service;

import java.util.Map;

public interface IKakaoService {

    public String getReturnAccessToken(String code);

    public Map<String,Object> getUserInfo(String access_token);
}
