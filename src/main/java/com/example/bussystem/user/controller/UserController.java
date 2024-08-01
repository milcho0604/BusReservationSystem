package com.example.bussystem.user.controller;


import com.example.bussystem.common.auth.JwtTokenProvider;
import com.example.bussystem.common.dto.CommonErrorDto;
import com.example.bussystem.common.dto.CommonResDto;
import com.example.bussystem.user.domain.User;
import com.example.bussystem.user.dto.UserListDto;
import com.example.bussystem.user.dto.UserLoginDto;
import com.example.bussystem.user.dto.UserRefreshDto;
import com.example.bussystem.user.dto.UserSaveDto;
import com.example.bussystem.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    @Qualifier("2")
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    public UserController(JwtTokenProvider jwtTokenProvider, UserService userService, RedisTemplate<String, Object> redisTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }
    @PostMapping("create")
    public ResponseEntity<?> userCreatePost(@Valid @RequestBody UserSaveDto dto){
        try {
            User user = userService.userCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "회원가입에 성공하였습니다.", user.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("doLogin")
    public ResponseEntity<?> doLogin(@RequestBody UserLoginDto dto) {
        // email, password 일치하는지 검증
        User user = userService.login(dto);
        // 일치할 경우 accessToken 생성
        String jwtToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRole().toString());

        // redis -> email, rt를 key:value로 하여 저장
        redisTemplate.opsForValue().set(user.getEmail(), refreshToken, 240, TimeUnit.HOURS); // 240 시간
        // 생성된 토큰을 CommonResDto 에 담아 사용자에게 return
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", user.getId());
        loginInfo.put("token", jwtToken);
        loginInfo.put("refreshToken", refreshToken);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "로그인에 성공하였습니다.", loginInfo);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<Object> userList(Pageable pageable) {
        Page<UserListDto> userListDtos = userService.userList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원 목록을 조회합니다.", userListDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAccessToken(@RequestBody UserRefreshDto dto) {
        String rt = dto.getRefreshToken();
        Claims claims;
        try {
            // 코드를 통해 rt 검증
            claims = Jwts.parser().setSigningKey(secretKeyRt).parseClaimsJws(rt).getBody(); // -> 이 한줄이 토큰 검증 코드
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.UNAUTHORIZED, "invalid refresh token"), HttpStatus.UNAUTHORIZED);
        }
        String email = claims.getSubject();
        String role = claims.get("role").toString();

        // redis 조회하여 rt 추가 검증
        Object obj = redisTemplate.opsForValue().get(email);
        if (obj == null || !obj.toString().equals(rt)){
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.UNAUTHORIZED, "invalid refresh token"), HttpStatus.UNAUTHORIZED);
        }

        String newAccessToken = jwtTokenProvider.createToken(email, role);
        Map<String, Object> info = new HashMap<>();
        info.put("token", newAccessToken);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "!!!AccessToken is renewed!!!", info);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
    @GetMapping("/myInfo")
    public ResponseEntity<Object> myInfo() {
        UserListDto dto = userService.myInfo();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "마이페이지로 이동합니다.", dto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}
