package com.example.login.controller;

import com.example.login.vo.UserVO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    protected MockHttpSession session;

    private MockHttpServletRequest servletRequest;

    @Autowired
    private MockMvc mvc;

    public void sessionSetUp() throws Exception{
        session = new MockHttpSession();

        UserVO userVo = new UserVO();

        userVo.setId("testId");
        userVo.setPassword("testPw");
        userVo.setName("testName");

        session.setAttribute("login", userVo);
    }

    @AfterEach
    public void sessionClean(){
        if(session != null){
            session.clearAttributes();
        }
    }

    @Test
    void testJoin() {
        try {
            UserVO userVo = new UserVO();
            userVo.setId("testId");
            userVo.setPassword("testPw");
            userVo.setName("testName");

            String jsonData = new Gson().toJson(userVo);

            mvc.perform(post("/api/user/")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonData))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.resultYn", "Y").exists());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLogin() {
        try {
            UserVO userVo = new UserVO();
            userVo.setId("testId");
            userVo.setPassword("testPw");

            String jsonData = new Gson().toJson(userVo);

            mvc.perform(post("/api/user/loginCheck")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonData))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.resultYn", "Y").exists());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSelectUser() {
        try {
            sessionSetUp();

            mvc.perform(get("/api/user/testId")
               .contentType(MediaType.APPLICATION_JSON)
               .session(session))
               .andExpect(status().isOk())
               .andDo(print());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDeleteUser() {
        try {
            sessionSetUp();

            mvc.perform(delete("/api/user/testId")
               .contentType(MediaType.APPLICATION_JSON)
               .session(session))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.resultYn", "Y").exists());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCheckUser() {
        try {

            mvc.perform(post("/api/user/check/testId")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.resultYn", "Y").exists());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void logout() {

    }


}