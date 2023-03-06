package com.example.login.common.intercepter;

import com.example.login.vo.UserVO;
import org.apache.catalina.User;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Component
public class LoginCheckIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserVO userVo = (UserVO) request.getSession().getAttribute("login");

        if (userVo != null) {
            return true;
        }

        request.setAttribute("exception", "AuthenticationException");
        request.getRequestDispatcher("/api/error").forward(request, response);

        return false;
    }

}
