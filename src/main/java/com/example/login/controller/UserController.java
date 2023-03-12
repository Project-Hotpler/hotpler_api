package com.example.login.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.login.common.api.ApiResult;
import com.example.login.dto.SearchDto.SearchRequestDto;
import com.example.login.dto.UserDto.UpdateRequestDto;
import com.example.login.dto.UserDto.UserResponseDto;
import com.example.login.dto.UserDto.UserListDto;
import com.example.login.dto.UserDto.LoginRequestDto;
import com.example.login.dto.UserDto.JoinRequestDto;
import io.swagger.annotations.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import com.example.login.service.LoginService;
import com.example.login.vo.UserVO;

@RestController
@RequestMapping("/api/user")
@Api(tags = "사용자")
public class UserController {

	// API 공통 return 값
	ApiResult apiResult;

	@Autowired
	LoginService service;

	@ApiOperation(value="회원 가입")
	@PostMapping("/")
	public ApiResult insertUser(@RequestBody JoinRequestDto joinRequestDto) {

		UserVO userVo = UserVO.builder()
				.id(joinRequestDto.getId())
				.name(joinRequestDto.getName())
				.password(joinRequestDto.getPassword())
				.build();

		return service.insertUser(userVo);
	}

	@ApiOperation(value="회원 수정")
	@ApiImplicitParam(name = "id", value = "사용자 아이디")
	@PutMapping("/{id}")
	public ApiResult updateUser(@PathVariable String id, @RequestBody UpdateRequestDto updateRequestDto) {

		UserVO userVo = UserVO.builder()
				.id(updateRequestDto.getId())
				.name(updateRequestDto.getName())
				.build();

		return service.updateUser(id, userVo);
	}

	@ApiOperation(value="회원 조회")
	@ApiImplicitParam(name = "id", value = "사용자 아이디")
	@GetMapping("/{id}")
	public UserResponseDto selectUser(@PathVariable String id) {

		UserVO userVo = service.selectUser(id);

		UserResponseDto userResponseDto = UserResponseDto.builder()
				.id(userVo.getId())
				.name(userVo.getName())
				.joinDate(userVo.getJoinDate())
				.autoLogin(userVo.isAutoLogin())
				.limitDate(userVo.getLimitDate())
				.build();

		return userResponseDto;
	}

	@ApiOperation(value="회원 리스트 조회")
	@ApiImplicitParams(
			{
					@ApiImplicitParam(name = "page", value = "현재 페이지"),
					@ApiImplicitParam(name = "recordSize", value = "페이지당 출력할 데이터 개수"),
					@ApiImplicitParam(name = "keyword", value = "검색 키워드"),
					@ApiImplicitParam(name = "searchType", value = "검색 유형")
			}
	)
	@GetMapping("/userList")
	public List<UserListDto> selectUserList(@ApiParam(value = "page", required = false) @RequestParam(value = "page", required = false, defaultValue = "0") int page,
											@ApiParam(value = "recordSize", required = false) @RequestParam(value = "recordSize", required = false, defaultValue = "10") int recordSize,
											@ApiParam(value = "keyword", required = false) @RequestParam(value = "keyword", required = false) String keyword,
											@ApiParam(value = "searchType", required = false) @RequestParam(value = "searchType", required = false) String searchType
											) {

		List<UserListDto> returnList = new ArrayList<>();

		SearchRequestDto searchRequestDto = SearchRequestDto.builder()
				.page(page)
				.recordSize(recordSize)
				.keyword(keyword)
				.searchType(searchType)
				.build();

		List<UserVO> userList = service.selectUserList(searchRequestDto);

		for (UserVO userVo : userList){

			UserListDto usersListDto = UserListDto.builder()
					.id(userVo.getId())
					.name(userVo.getName())
					.joinDate(userVo.getJoinDate())
					.autoLogin(userVo.isAutoLogin())
					.limitDate(userVo.getLimitDate())
					.build();

			returnList.add(usersListDto);
		}

		return returnList;
	}

	@ApiOperation(value="회원 탈퇴")
	@ApiImplicitParam(name = "id", value = "사용자 아이디")
	@DeleteMapping("/{id}")
	public ApiResult deleteUser(@PathVariable String id) {

		return service.deleteUser(id);
	}


	@ApiOperation(value="로그인")
	@PostMapping("/login")
	public ApiResult login(@RequestBody LoginRequestDto loginRequestDto , HttpSession session , HttpServletResponse response) {

		return service.userLoginCheck(loginRequestDto, service.selectUser(loginRequestDto.getId()), response, session);
	}

	@ApiOperation(value="로그 아웃")
	@PostMapping("/logout")
	public ApiResult logout(HttpSession session , HttpServletRequest request , HttpServletResponse response) {

		return service.userLogout((UserVO)session.getAttribute("login"), request, response, session);
	}

	@ApiOperation(value="아이디 중복 확인")
	@ApiImplicitParam(name = "id", value = "사용자 아이디")
	@PostMapping("/check/{id}")
	public ApiResult checkUser(@PathVariable String id) {

		return service.checkUserId(id);
	}
	

}
