package com.example.bussystem.user.dto;

import com.example.bussystem.common.domain.Address;
import com.example.bussystem.user.domain.Role;
import com.example.bussystem.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSaveDto {

    private String name;
    @NotEmpty(message = "email is essential")
    private String email;
    @NotEmpty(message = "password is essential")
    @Size(min = 8, message = "비밀번호는 최소 8자리입니다.")
    private String password;
    private Long age;
    private String phone;
    private Address address;
    private Role role = Role.USER;

    public User toEntity(String password) {
        return User.builder()
                .password(password)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .age(this.age)
                .address(this.address)
                .role(this.role)
                .build();
    }
}
