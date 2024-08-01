package com.example.bussystem.reservation.repository;

import com.example.bussystem.reservation.domain.Reservation;
import com.example.bussystem.user.domain.User;
import com.example.bussystem.user.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
}
