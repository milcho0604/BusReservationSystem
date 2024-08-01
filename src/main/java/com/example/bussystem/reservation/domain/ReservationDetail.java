package com.example.bussystem.reservation.domain;

import com.example.bussystem.bus.domain.Bus;
import com.example.bussystem.reservation.dto.ReservationListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class ReservationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id")
    private Bus bus;


    public ReservationListDto.ReservationDetailDto fromEntity() {
        ReservationListDto.ReservationDetailDto reservationDetailDto = ReservationListDto.ReservationDetailDto.builder()
                .id(this.id)
                .busNo(this.bus.getBusNo())
                .ticketCount(this.quantity)
                .build();
        return reservationDetailDto;
    }
}
