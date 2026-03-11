package com.miracle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String userName;
    private String email;
    private String password;
    private String userImg;
    private String role;
    private Boolean isActive;
}
