package com.example.login.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.login.repository.IUserMapper;
import com.example.login.vo.UserVO;

@Service
public class LoginService implements ILoginService {

	@Autowired
	IUserMapper mapper;
	
	@Override
	public void insertUser(UserVO vo) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String securePw = encoder.encode(vo.getPassword());
		vo.setPassword(securePw);

		mapper.insertUser(vo);
	}

	@Override
	public int checkUserId(String id) {
		
		return mapper.checkUserId(id);
	}
	
	@Override
	public void deleteUser(String id) {
		
		mapper.deleteUser(id);
	}

	@Override
	public UserVO selectUser(String id) {
		
		return mapper.selectUser(id);
	}

	@Override
	public List<UserVO> selectUserList() {
		return null;
	}

	@Override
	public void userAutoLogin(String sessionId,Date limitDate,String id) {
		
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



}
