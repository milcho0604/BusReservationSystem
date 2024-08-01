package com.example.bussystem.user.dto;


import com.example.bussystem.common.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {
    private Long id;
    private String email;
    private String name;
    private Long age;
    private String phone;
    private Address address;



}
