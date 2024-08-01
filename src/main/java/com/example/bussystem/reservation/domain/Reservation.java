package com.example.bussystem.reservation.domain;


import com.example.bussystem.reservation.dto.ReservationListDto;
import com.example.bussystem.user.domain.User;
import com.example.bussystem.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReservationStatus reservationStatus = ReservationStatus.ORDERED;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "reservation", cascade = CascadeType.PERSIST, orphanRemoval = true)
    // 빌더패턴에서도 ArrayList로 초기화 되도록 하는 설정
    @Builder.Default
    private List<ReservationDetail> reservationDetails = new ArrayList<>();

    public ReservationListDto fromEntity() {
        List<ReservationDetail> reservationDetailList = this.getReservationDetails();
        List<ReservationListDto.ReservationDetailDto> reservationDetailDtos = new ArrayList<>();
        for (ReservationDetail reservationDetail : reservationDetailList) {
            reservationDetailDtos.add(reservationDetail.fromEntity());
        }

        ReservationListDto reservationListDto = ReservationListDto.builder()
                .id(this.id)
                .reservationStatus(this.reservationStatus)
                .userEmail(this.user.getEmail())
                .reservationDetailDtos(reservationDetailDtos)
                .build();
        return reservationListDto;
    }

    public void updateStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

}
