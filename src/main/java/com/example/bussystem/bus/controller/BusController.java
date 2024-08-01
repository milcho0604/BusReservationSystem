package com.example.bussystem.bus.controller;

import com.example.bussystem.bus.domain.Bus;
import com.example.bussystem.bus.dto.BusListDto;
import com.example.bussystem.bus.dto.BusSaveDto;
import com.example.bussystem.bus.service.BusService;
import com.example.bussystem.common.dto.CommonErrorDto;
import com.example.bussystem.common.dto.CommonResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("bus")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("create")
    public ResponseEntity<?> busCreatePost(@ModelAttribute BusSaveDto dto,  @RequestParam MultipartFile busImage){
        try {
            Bus bus = busService.busCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "버스등록에 성공하였습니다.", bus.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> busList(Pageable pageable){
        Page<BusListDto> busListDtos = busService.busList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "버스 목록을 조회합니다.", busListDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/aws/create")
    public ResponseEntity<?> busAwsCreatePost(@ModelAttribute BusSaveDto dto, @RequestParam MultipartFile busImage){
        try {
            Bus bus = busService.busAwsCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "버스 등록에 성공하였습니다.", bus.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
