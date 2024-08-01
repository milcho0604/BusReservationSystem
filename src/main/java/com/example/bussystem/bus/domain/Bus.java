package com.example.bussystem.bus.domain;


import com.example.bussystem.bus.dto.BusListDto;
import com.example.bussystem.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private String lastStop;

    private String firstBus;

    private String lastBus;

    private Integer ticket_quantity;

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
                .ticket_quantity(ticket_quantity)
                .build();
    }

    public void UpdatTicketQuantity(int quantity){
        this.ticket_quantity -= quantity;
    }

}
