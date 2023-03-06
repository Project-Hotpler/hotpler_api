package com.example.login;

import java.util.Arrays;
import java.util.List;

import com.example.login.common.intercepter.LoginCheckIntercepter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.login.common.intercepter.AutoLoginIntercepter;

@Configuration
public class ServerConfigure implements WebMvcConfigurer{

	// 자동 로그인 시 로그인 세션 부여 URL
	String[] AutoLoginUrl = {
			"/api/test"
	};
	List<String> autoLoginUrlList = Arrays.asList(AutoLoginUrl);

	// 로그인 검증 안해도 되는 URL
	String[] loginCheckUrl = {
			"/api/user/loginCheck", "/api/user/", "/api/user/check/{id}"
	};
	List<String> loginCheckUrlList = Arrays.asList(loginCheckUrl);

	//인터셉터가 동작할 주소 세팅
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 자동 로그인
		registry.addInterceptor(new AutoLoginIntercepter()).addPathPatterns(autoLoginUrlList);

		// 로그인 체크
		registry.addInterceptor(new LoginCheckIntercepter()).addPathPatterns("/api/user/**/")
															.excludePathPatterns(loginCheckUrl);
	}
}