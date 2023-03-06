package com.example.login.common.intercepter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.example.login.service.LoginService;
import com.example.login.vo.UserVO;

@SuppressWarnings("deprecation")
@Component
public class AutoLoginIntercepter extends HandlerInterceptorAdapter{
	
	@Autowired
	LoginService service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 HttpSession session = request.getSession();
		 
		 Cookie cookie = WebUtils.getCookie(request,"loginCookie");
		 if(cookie != null) {
			 String cookieId = cookie.getValue();
			 if(cookieId != null) {
				 UserVO user = service.selectUserWithSessionId(cookieId);
				 session.setAttribute("login", user);
			 }
		 }
		
		return true;
	}
}
