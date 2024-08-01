package com.example.bussystem.bus.dto;

import com.example.bussystem.bus.domain.Bus;
import com.example.bussystem.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusSaveDto {

    private String busNo;

    private String firstStop;

    private String stopover;

    private String lastStop;

    private Integer stopoverCount;

    private String firstBus;

    private String lastBus;

    private MultipartFile busImage;

    private String categoryName;

    private Long price;

    private String schedule;

    private Integer ticket_quantity;

    public Bus toEntity(Category category){
        return Bus.builder()
                .busNo(this.busNo)
                .firstBus(this.firstBus)
                .stopover(this.stopover)
                .stopoverCount(this.stopoverCount)
                .lastBus(this.lastBus)
                .firstStop(this.firstStop)
                .lastStop(this.lastStop)
                .category(category)
                .price(this.price)
                .schedule(this.schedule)
                .ticket_quantity(ticket_quantity)
                .build();
    }
}
