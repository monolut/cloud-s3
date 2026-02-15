package com.clouds3.userservice.dto;

import com.clouds3.userservice.enums.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long id;

    @NotNull(message = "Role name cannot be null")
    private Role role;
}
