package com.example.bussystem.reservation.service;


import com.example.bussystem.bus.domain.Bus;
import com.example.bussystem.bus.repository.BusRepository;
import com.example.bussystem.common.service.StockInventoryService;
import com.example.bussystem.reservation.domain.Reservation;
import com.example.bussystem.reservation.domain.ReservationDetail;
import com.example.bussystem.reservation.domain.ReservationStatus;
import com.example.bussystem.reservation.dto.ReservationListDto;
import com.example.bussystem.reservation.dto.ReservationSaveDto;
import com.example.bussystem.reservation.event.StockDecreaseEvent;
import com.example.bussystem.reservation.repository.ReservationRepository;
import com.example.bussystem.user.domain.User;
import com.example.bussystem.user.dto.UserListDto;
import com.example.bussystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BusRepository busRepository;
    private final StockInventoryService stockInventoryService;
    private final StockDecreaseEventHandler stockDecreaseEventHandler;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, BusRepository busRepository, StockInventoryService stockInventoryService, StockDecreaseEventHandler stockDecreaseEventHandler) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.busRepository = busRepository;
        this.stockInventoryService = stockInventoryService;
        this.stockDecreaseEventHandler = stockDecreaseEventHandler;
    }

    public Reservation reservationCreate(@ModelAttribute List<ReservationSaveDto> dto){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
        Reservation reservation = Reservation.builder()
                .user(user)
                .build();
        for (ReservationSaveDto dtos : dto ){
            Bus bus = busRepository.findById(dtos.getBusId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 버스입니다."));
            int quantity = dtos.getTicketCount();
            if (bus.getCategory().getCategoryName().contains("직행")){
                int newQuantity = stockInventoryService.decreaseStock(dtos.getBusId(), dtos.getTicketCount()).intValue();
                if(newQuantity<0){
                    throw new IllegalArgumentException("(redis) 재고가 부족합니다.");
                }
                stockDecreaseEventHandler.publish(new StockDecreaseEvent(bus.getId(), dtos.getTicketCount()));
            }
            else {
                if (quantity > bus.getTicket_quantity()) {
                    throw new IllegalArgumentException("재고가 부족합니다");
                } else {
                    // 변경감지로 인해 별도의 save 불필요
                    bus.updateTicketQuantity(quantity);
                }
            }
            ReservationDetail reservationDetails = ReservationDetail.builder()
                    .bus(bus)
                    .quantity(quantity)
                    .reservation(reservation)
                    .build();
            reservation.getReservationDetails().add(reservationDetails);
        }
        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation;
    }

    @Transactional
    public List<ReservationListDto> reservationList() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationListDto> reservationListDtos = new ArrayList<>();
        for (Reservation reservation : reservations){
            reservationListDtos.add(reservation.fromEntity());
        }
        return reservationListDtos;
    }

    @Transactional
    public Reservation reservationCancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));
        reservation.updateStatus(ReservationStatus.CANCELED);

        return reservation;
    }

    @Transactional
    public List<ReservationListDto> myOrders() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        List<Reservation> reservations = reservationRepository.findByUser(user);
        List<ReservationListDto> reservationListDtos  = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationListDtos.add(reservation.fromEntity());
        }
        return reservationListDtos;
    }
}
