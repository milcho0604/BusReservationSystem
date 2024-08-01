package com.example.bussystem.common.service;


import com.example.bussystem.user.domain.Role;
import com.example.bussystem.user.dto.UserSaveDto;
import com.example.bussystem.user.repository.UserRepository;
import com.example.bussystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


// CommandLineRunner 상속함으로써 해당 컴포넌트가 스프링빈으로 등록되는 시점에 run 메서드 실행
@Component
public class InitialDataLoader implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@test.com").isEmpty()) {
            userService.userCreate(UserSaveDto.builder()
                    .name("admin")
                    .email("admin@test.com")
                    .password("12341234")
                    .role(Role.ADMIN)
                    .build());
        }
    }
}
