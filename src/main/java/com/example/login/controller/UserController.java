package com.example.login.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.login.common.api.ApiResult;
import com.example.login.dto.UserDto.LoginRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.example.login.service.LoginService;
import com.example.login.vo.UserVO;

@RestController
@RequestMapping("/api/user")
@Api(tags = "사용자")
public class UserController {

	// API return 값
	ApiResult apiResult;

	@Autowired
	LoginService service;

	@ApiOperation(value="회원 가입")
	@PostMapping("/")
	public ApiResult insertUser(@RequestBody UserVO vo) {

		service.insertUser(vo);

		apiResult = ApiResult.builder()
				.resultYn("Y")
				.message("")
				.build();

		return apiResult;
	}

	@ApiOperation(value="회원 조회")
	@GetMapping("/{id}")
	public UserVO selectUser(@PathVariable String id) {

		return service.selectUser(id);
	}

	@ApiOperation(value="회원 탈퇴")
	@DeleteMapping("/{id}")
	public ApiResult deleteUser(@PathVariable String id) {

		service.deleteUser(id);

		apiResult = ApiResult.builder()
				.resultYn("Y")
				.message("")
				.build();

		return apiResult;
	}


	@ApiOperation(value="로그인")
	@PostMapping("/loginCheck")
	public ApiResult login(@RequestBody LoginRequestDto loginRequest , HttpSession session , HttpServletResponse response) {

		BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

		UserVO db = service.selectUser(loginRequest.getId());
		if(db != null) {
			if(encode.matches(loginRequest.getPassword(),db.getPassword())) {
				session.setAttribute("login",db);
				
				//자동로그인을 체크했을시에 실행
				if(loginRequest.isAutoLogin()) {
					
					//3개월뒤의 초
					long second = 60 * 60 * 24 * 90;
					
					//쿠키생성
					Cookie cookie = new Cookie("loginCookie",session.getId());
					cookie.setPath("/");
					cookie.setMaxAge((int)second);
				    response.addCookie(cookie);
				    
				    //3개월뒤의 밀리초를 날짜로 변환
				    long millis = System.currentTimeMillis() + (second * 1000); 
				    Date limitDate = new Date(millis);
				    
				    //DB에 세션아이디,쿠키만료날짜,회원 아이디 전달
				    service.userAutoLogin(session.getId(),limitDate,loginRequest.getId());
				}
				apiResult = ApiResult.builder()
						.resultYn("Y")
						.message("loginSuccess")
						.build();

			}else {
				apiResult = ApiResult.builder()
						.resultYn("N")
						.message("pwFail")
						.build();
			}
		}else {
			apiResult = ApiResult.builder()
					.resultYn("N")
					.message("idFail")
					.build();
		}

		return apiResult;
	}

	@ApiOperation(value="로그 아웃")
	@PostMapping("/logout")
	public ApiResult logout(HttpSession session , HttpServletRequest request , HttpServletResponse response) {
		
		UserVO user = (UserVO)session.getAttribute("login");

		if(user!= null) {
			session.removeAttribute("login"); 
			session.invalidate();

			Cookie cookie = WebUtils.getCookie(request,"loginCookie");
			
			//자동로그인을 한 상태의 사용자가 로그아웃을 할 경우
			if(cookie != null) {
	
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				service.userAutoLogin("none", new Date(), user.getId());
			}
		}

		apiResult = ApiResult.builder()
				.resultYn("Y")
				.message("")
				.build();

		return apiResult;
	}

	@ApiOperation(value="아이디 중복 확인")
	@PostMapping("/check/{id}")
	public ApiResult checkUser(@PathVariable String id) {

		int num = service.checkUserId(id);

		//변수 num이 1일경우 아이디 중복 0일경우 아이디 등록가능
		if(num == 1) {
			apiResult = ApiResult.builder()
					.resultYn("N")
					.message("id exists")
					.build();
		}else {
			apiResult = ApiResult.builder()
					.resultYn("Y")
					.message("id not exists")
					.build();
		}
		return apiResult;
	}
	

}
