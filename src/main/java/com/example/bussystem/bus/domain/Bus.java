package com.example.bussystem.bus.domain;

import com.example.bussystem.bus.dto.BusDetailDto;
import com.example.bussystem.bus.dto.BusListDto;
import com.example.bussystem.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busNo;

    private String firstStop;

    private String stopover;

    private String lastStop;

    private Integer stopoverCount;

    private String firstBus;

    private String lastBus;

    private Long price;

    private Integer ticket_quantity;

    private String schedule;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String imagePath;

    public void updateImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public BusListDto listFromEntity(){
        return BusListDto.builder()
                .id(this.id)
                .busNo(this.busNo)
                .firstStop(this.firstStop)
                .lastStop(this.lastStop)
                .firstBus(this.firstBus)
                .lastBus(this.lastBus)
                .category(this.category)
                .price(this.price)
                .ticket_quantity(ticket_quantity)
                .build();
    }

    public BusDetailDto detailFromEntity(){
        return BusDetailDto.builder()
                .id(this.id)
                .busNo(this.busNo)
                .firstStop(this.firstStop)
                .stopover(this.stopover)
                .lastStop(this.lastStop)
                .stopoverCount(this.stopoverCount)
                .firstBus(this.firstBus)
                .lastBus(this.lastBus)
                .categoryName(this.category.getCategoryName())
                .price(this.price)
                .schedule(this.schedule)
                .imagePath(this.imagePath)
                .ticket_quantity(ticket_quantity)
                .build();
    }

    public void updateTicketQuantity(int quantity){
        this.ticket_quantity -= quantity;
    }

}
