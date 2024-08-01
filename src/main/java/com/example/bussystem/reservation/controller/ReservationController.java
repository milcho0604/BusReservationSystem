package com.example.bussystem.reservation.controller;

import com.example.bussystem.common.dto.CommonResDto;
import com.example.bussystem.reservation.domain.Reservation;
import com.example.bussystem.reservation.dto.ReservationListDto;
import com.example.bussystem.reservation.dto.ReservationSaveDto;
import com.example.bussystem.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reserve")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("create")
    public ResponseEntity<?> reservationCreate(@RequestBody List<ReservationSaveDto> dto) {
        Reservation reservation = reservationService.reservationCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "예약완료", reservation.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("list")
    public ResponseEntity<?> reservationList() {
        List<ReservationListDto> reservationList = reservationService.reservationList();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "예약 목록 조회", reservationList);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/cancel")
    public ResponseEntity<?> reservationCancel(@PathVariable Long id){
        Reservation reservation = reservationService.reservationCancel(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "예약이 취소되었습니다.", reservation.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("myOrders")
    public ResponseEntity<?> myOrders(){
        List<ReservationListDto> dto = reservationService.myOrders();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,
                SecurityContextHolder.getContext().getAuthentication().getName()+"회원의 예약 목록을 조회합니다.",
                dto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

}
