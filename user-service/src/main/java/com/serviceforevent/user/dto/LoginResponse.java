package com.serviceforevent.user.dto;

import com.serviceforevent.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private User user;
} 