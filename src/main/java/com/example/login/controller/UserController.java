package com.example.login.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.login.service.UserService;
import com.example.login.vo.UserVO;

@RestController
@RequestMapping("/api/user")
@Api(tags = "사용자")
public class UserController {

	// API 공통 return 값
	ApiResult apiResult;

	@Autowired
	UserService service;

	@ApiOperation(value="회원 가입", notes="")
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
	public List<UserListDto> selectUserList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
											@RequestParam(value = "recordSize", required = false, defaultValue = "10") int recordSize,
											@RequestParam(value = "keyword", required = false) String keyword,
											@RequestParam(value = "searchType", required = false) String searchType
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


	@ApiOperation(value="로그인", notes="Status Fail Case"
			+ "\n 1. 아이디가 틀렸을 때 -> message: idFail"
			+ "\n 2. 비밀번호가 틀렸을 때 -> message: pwFail")
	@PostMapping("/login")
	public ApiResult login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {

		return service.userLoginCheck(loginRequestDto, service.selectUser(loginRequestDto.getId()), response, request);
	}

	@ApiOperation(value="로그 아웃")
	@PostMapping("/logout")
	public ApiResult logout(HttpServletRequest request, HttpServletResponse response) {

		return service.userLogout((UserVO)request.getSession().getAttribute("login"), request, response, request.getSession());
	}

	@ApiOperation(value="아이디 중복 확인", notes="Status Fail Case"
			+ "\n 1. 아이디가 존재할 때 -> message: id exists")
	@ApiImplicitParam(name = "id", value = "사용자 아이디")
	@PostMapping("/check/{id}")
	public ApiResult checkUser(@PathVariable String id) {

		return service.checkUserId(id);
	}
	

}
