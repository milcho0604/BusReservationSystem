package com.example.bussystem.user.dto;


import com.example.bussystem.common.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListDto {
    private Long id;
    private String email;
    private String name;
    private Address address;
}
