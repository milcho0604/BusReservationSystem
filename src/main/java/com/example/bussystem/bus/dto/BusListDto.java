package com.example.bussystem.bus.dto;

import com.example.bussystem.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusListDto {
    private Long id;

    private String busNo;

    private String firstStop;

    private String lastStop;

    private String firstBus;

    private String lastBus;

    private Category category;

    private Long price;

    private Integer ticket_quantity;
}
