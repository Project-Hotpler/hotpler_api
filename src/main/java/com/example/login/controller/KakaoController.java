package com.example.login.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.login.common.api.ApiResult;
import com.example.login.dto.UserDto.KakaoJoinRequestDto;
import com.example.login.service.KakaoService;
import com.example.login.service.UserService;
import com.example.login.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/user/kakao")
@Api(tags = "사용자(카카오)")
public class KakaoController {
    private KakaoService kakaoService;
    private UserService service;

    // API 공통 return 값
    ApiResult apiResult;

    @ApiOperation(value="로그인(카카오)", notes="Status Fail Case"
            + "\n 1. 회원가입이 필요할 때 -> message: join required")
    @ApiImplicitParam(name = "kakaoToken", value = "카카오 서버로부터 받은 Access Token")
    @GetMapping("/kakaoLogin")
    public ApiResult redirectKakao(@RequestParam String kakaoToken, HttpServletRequest request) throws IOException {

        /*System.out.println("============ code ============");
        System.out.println("code:: " + code);

        // 접속토큰 get
        String kakaoToken = kakaoService.getReturnAccessToken(code);*/

        // 접속자 정보 get
        Map<String, Object> result = kakaoService.getUserInfo(kakaoToken);

        System.out.println("============ result ============");
        System.out.println(result);

        String snsId = (String) result.get("id");
        String userName = (String) result.get("nickname");
        String email = (String) result.get("email");
        String userpw = snsId;

        // 분기
        UserVO userVo = service.selectUser(snsId);

        if (userVo == null) {

            apiResult = ApiResult.builder()
                    .status("fail")
                    .message("join required")
                    .build();

            userVo.setId(snsId);
            userVo.setPassword(userpw);

            request.getSession().setAttribute("kakaoLogin","userVo");

        } else{

            request.getSession().setAttribute("login", userVo);
            /* 로그아웃 처리 시, 사용할 토큰 값 */
            request.getSession().setAttribute("kakaoToken", kakaoToken);

            apiResult = ApiResult.builder()
                    .status("success")
                    .message("loginSuccess")
                    .build();
        }

        return apiResult;

    }

    @ApiOperation(value="회원 가입(카카오)", notes="Status Fail Case"
            + "\n 1. 카카오 로그인이 필요할 때 -> message: kakaoLogin required")
    @PostMapping("/")
    public ApiResult insertKakaoUser(@RequestBody KakaoJoinRequestDto kakaoJoinRequestDto, HttpServletRequest request) {

        UserVO kakaoLogin = (UserVO) request.getSession().getAttribute("kakaoLogin");

        if(kakaoLogin != null){

            UserVO userVo = UserVO.builder()
                    .id(kakaoLogin.getId())
                    .password(kakaoLogin.getPassword())
                    .name(kakaoJoinRequestDto.getId())
                    .build();

            return service.insertUser(userVo);

        } else{
            apiResult = ApiResult.builder()
                    .status("fail")
                    .message("kakaoLogin required")
                    .build();

            return apiResult;
        }

    }

}