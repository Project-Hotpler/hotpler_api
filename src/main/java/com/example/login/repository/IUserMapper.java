package com.example.login.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.login.dto.SearchDto;
import com.example.login.dto.SearchDto.SearchRequestDto;
import com.example.login.dto.UserDto.UserListDto;
import com.example.login.dto.UserDto.UserResponseDto;
import com.example.login.dto.UserDto.JoinRequestDto;
import org.apache.ibatis.annotations.Mapper;

import com.example.login.vo.UserVO;

@Mapper
public interface IUserMapper {
	
	//회원정보 등록
	void insertUser(UserVO userVo);

	//회원정보 수정
	void updateUser(Map<String,Object> map);

	//회원정보 조회
	UserVO selectUser(String id);

	//회원정보 전체조회
	List<UserVO> selectUserList(SearchRequestDto searchRequestDto);

	//회원 수 조회
	List<UserVO> countUser(SearchRequestDto searchRequestDto);

	//회원 탈퇴
	void deleteUser(String id);

	//아이디 중복확인
	int checkUserId(String id);
	
	//특정 회원의 세션아이디와 쿠키 유효기간을 저장
	void userAutoLogin(Map<String,Object> map);
	
	// 로그인 체크
	UserVO selectUserWithSessionId(String sessionId);
}
