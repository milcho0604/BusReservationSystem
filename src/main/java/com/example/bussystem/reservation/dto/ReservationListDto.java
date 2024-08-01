package com.example.bussystem.reservation.dto;

import com.example.bussystem.reservation.domain.Reservation;
import com.example.bussystem.reservation.domain.ReservationDetail;
import com.example.bussystem.reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReservationListDto {


    private Long id;
    private String userEmail;
    private ReservationStatus reservationStatus;
    private List<ReservationDetailDto> reservationDetailDtos ;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationDetailDto{
        private Long id;
        private String busNo;
        private Integer ticketCount;
    }
}
