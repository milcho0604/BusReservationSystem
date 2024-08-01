package com.example.bussystem.reservation.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDecreaseEvent {
    private Long busId;
    private Integer ticketCount;
}
