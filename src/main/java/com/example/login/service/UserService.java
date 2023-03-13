package com.example.login.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.login.common.api.ApiResult;
import com.example.login.dto.SearchDto.SearchRequestDto;
import com.example.login.dto.UserDto.LoginRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.login.repository.IUserMapper;
import com.example.login.vo.UserVO;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class UserService implements IUserService {

	// API 공통 return 값
	ApiResult apiResult;

	@Autowired
	IUserMapper mapper;
	
	@Override
	public ApiResult insertUser(UserVO userVo) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userVo.setPassword(encoder.encode(userVo.getPassword()));

		mapper.insertUser(userVo);

		apiResult = ApiResult.builder()
				.status("success")
				.message("")
				.build();

		return apiResult;
	}

	@Override
	public ApiResult updateUser(String userId, UserVO userVo) {

		Map<String,Object> map = new HashMap<>();

		map.put("userId",userId);
		map.put("userVo",userVo);

		mapper.updateUser(map);

		apiResult = ApiResult.builder()
				.status("success")
				.message("")
				.build();

		return apiResult;
	}

	@Override
	public ApiResult checkUserId(String id) {

		int num = mapper.checkUserId(id);

		//변수 num이 1일경우 아이디 중복 0일경우 아이디 등록가능
		if(num == 1) {
			apiResult = ApiResult.builder()
					.status("fail")
					.message("id exists")
					.build();
		}else {
			apiResult = ApiResult.builder()
					.status("success")
					.message("id not exists")
					.build();
		}

		return apiResult;
	}
	
	@Override
	public ApiResult deleteUser(String id) {
		
		mapper.deleteUser(id);

		apiResult = ApiResult.builder()
				.status("success")
				.message("")
				.build();

		return apiResult;
	}

	@Override
	public UserVO selectUser(String id) {
		
		return mapper.selectUser(id);
	}

	@Override
	public List<UserVO> selectUserList(SearchRequestDto searchRequestDto) {
		return mapper.selectUserList(searchRequestDto);
	}

	@Override
	public List<UserVO> countUser(SearchRequestDto searchRequestDto) {
		return mapper.countUser(searchRequestDto);
	}

	@Override
	public void userAutoLogin(String sessionId, Date limitDate, String id) {
		
		Map<String,Object> map = new HashMap<>();

		map.put("sessionId",sessionId);
		map.put("limitDate",limitDate);
		map.put("id",id);
		
		mapper.userAutoLogin(map);
		
	}

	@Override
	public UserVO selectUserWithSessionId(String sessionId) {
		return mapper.selectUserWithSessionId(sessionId);
	}

	@Override
	public ApiResult userLoginCheck(LoginRequestDto loginRequestDto, UserVO userVo, HttpServletResponse response, HttpServletRequest request) {

		if(userVo != null) {

			BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

			if(encode.matches(loginRequestDto.getPassword(),userVo.getPassword())) {
				request.getSession().setAttribute("login",userVo);

				//자동로그인을 체크했을시에 실행
				if(loginRequestDto.isAutoLogin()) {

					//3개월뒤의 초
					long second = 60 * 60 * 24 * 90;

					//쿠키생성
					Cookie cookie = new Cookie("loginCookie", request.getSession().getId());
					cookie.setPath("/");
					cookie.setMaxAge((int)second);
					response.addCookie(cookie);

					//3개월뒤의 밀리초를 날짜로 변환
					long millis = System.currentTimeMillis() + (second * 1000);
					Date limitDate = new Date(millis);

					//DB에 세션아이디,쿠키만료날짜,회원 아이디 전달
					userAutoLogin(request.getSession().getId(), limitDate, loginRequestDto.getId());
				}

				apiResult = ApiResult.builder()
						.status("success")
						.message("loginSuccess")
						.build();

			}else {
				apiResult = ApiResult.builder()
						.status("fail")
						.message("pwFail")
						.build();
			}
		}else {
			apiResult = ApiResult.builder()
					.status("fail")
					.message("idFail")
					.build();
		}

		return apiResult;

	}

	@Override
	public ApiResult userLogout(UserVO userVo, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		if(userVo != null) {
			session.removeAttribute("login");
			session.invalidate();

			Cookie cookie = WebUtils.getCookie(request,"loginCookie");

			//자동로그인을 한 상태의 사용자가 로그아웃을 할 경우
			if(cookie != null) {

				cookie.setMaxAge(0);
				response.addCookie(cookie);
				userAutoLogin("none", new Date(), userVo.getId());
			}
		}

		apiResult = ApiResult.builder()
				.status("success")
				.message("")
				.build();

		return apiResult;
	}
}
