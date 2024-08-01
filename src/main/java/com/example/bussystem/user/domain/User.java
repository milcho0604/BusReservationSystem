package com.example.bussystem.user.domain;


import com.example.bussystem.common.domain.Address;
import com.example.bussystem.user.dto.UserDetailDto;
import com.example.bussystem.user.dto.UserListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    @Embedded
    private Address address;
    private String name;
    private Long age;
    private String phone;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    public UserListDto listFromEntity(){
        return UserListDto.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .address(this.address)
                .build();
    }

    public UserDetailDto detailFromEntity(){
        return UserDetailDto.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .age(this.age)
                .address(this.address)
                .build();
    }


}
