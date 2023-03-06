package com.example.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class LoginRequestDto{

        private String id;
        private String password;

        private boolean autoLogin;

        @Builder
        public LoginRequestDto(String id, String password, boolean autoLogin) {
            this.id = id;
            this.password = password;
            this.autoLogin = autoLogin;
        }
    }
}
