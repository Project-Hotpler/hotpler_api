package com.example.login.service;

import java.util.Date;
import java.util.List;

import com.example.login.vo.UserVO;

public interface ILoginService {
		
		//회원정보 등록
		void insertUser(UserVO vo);

		//회원정보 조회
		UserVO selectUser(String id);

		//회원정보 전체조회
		List<UserVO> selectUserList();

		//회원 탈퇴
		void deleteUser(String id);

		//아이디 중복확인
		int checkUserId(String id);

		//특정 회원의 세션아이디와 쿠키 유효기간을 저장
		void userAutoLogin(String sessionId,Date limitDate,String id);
		
		//세션아이디로 회원조회
		UserVO selectUserWithSessionId(String sessionId);
}
