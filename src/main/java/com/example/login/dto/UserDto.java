package com.example.login.dto;

import com.example.login.vo.UserVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class JoinRequestDto{

        private String id;

        private String password;

        private String name;

        @Builder
        public JoinRequestDto(String id, String password, String name) {
            this.id = id;
            this.password = password;
            this.name = name;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRequestDto{

        private String id;

        private String name;

        @Builder
        public UpdateRequestDto(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

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

    @Getter
    @NoArgsConstructor
    public static class UserResponseDto{

        private String id;

        private String name;

        private Date joinDate;

        private boolean autoLogin;

        private Date limitDate;

        @Builder
        public UserResponseDto(String id, String name, Date joinDate, boolean autoLogin, Date limitDate) {
            this.id = id;
            this.name = name;
            this.joinDate = joinDate;
            this.autoLogin = autoLogin;
            this.limitDate = limitDate;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UserListDto{

        private String id;

        private String name;

        private Date joinDate;

        private boolean autoLogin;

        private Date limitDate;

        @Builder
        public UserListDto(String id, String name, Date joinDate, boolean autoLogin, Date limitDate) {
            this.id = id;
            this.name = name;
            this.joinDate = joinDate;
            this.autoLogin = autoLogin;
            this.limitDate = limitDate;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class KakaoJoinRequestDto{

        private String id;

        private String name;

        @Builder
        public KakaoJoinRequestDto(String id, String password, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
