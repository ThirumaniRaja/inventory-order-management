package com.guvi.inventory.DTO;

import com.guvi.inventory.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private Long userId;
    private String username;
    private String email;
    private Role role;
    private String message;
}

