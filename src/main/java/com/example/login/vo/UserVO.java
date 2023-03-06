package com.example.login.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserVO{
	private String id;
	private String password;
	private String name;
	private Date joinDate;
	private boolean autoLogin;
	private String sessionId;
	private Date limitDate;

	@Builder
	public UserVO(String id, String password, String name, Date joinDate, boolean autoLogin, String sessionId, Date limitDate) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.joinDate = joinDate;
		this.autoLogin = autoLogin;
		this.sessionId = sessionId;
		this.limitDate = limitDate;
	}
}
