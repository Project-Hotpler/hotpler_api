package com.example.login.service;

import java.util.Date;
import java.util.List;

import com.example.login.common.api.ApiResult;
import com.example.login.dto.SearchDto.SearchRequestDto;
import com.example.login.dto.UserDto.LoginRequestDto;
import com.example.login.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface IUserService {
		
		// 회원정보 등록
		ApiResult insertUser(UserVO userVo);

		//회원정보 수정
		ApiResult updateUser(String userId, UserVO userVo);

		// 회원정보 조회
		UserVO selectUser(String id);

		// 회원정보 전체조회
		List<UserVO> selectUserList(SearchRequestDto searchRequestDto);

		//회원 수 조회
		List<UserVO> countUser(SearchRequestDto searchRequestDto);

		// 회원 탈퇴
		ApiResult deleteUser(String id);

		// 아이디 중복확인
		ApiResult checkUserId(String id);

		// 특정 회원의 세션아이디와 쿠키 유효기간을 저장
		void userAutoLogin(String sessionId,Date limitDate,String id);
		
		// 세션아이디로 회원조회
		UserVO selectUserWithSessionId(String sessionId);

		// 로그인 성공 여부 체크
	 	ApiResult userLoginCheck(LoginRequestDto loginRequestDto, UserVO userVo, HttpServletResponse response, HttpServletRequest request);

		// 로그아웃
		ApiResult userLogout(UserVO userVo, HttpServletRequest request, HttpServletResponse response, HttpSession session);

}
