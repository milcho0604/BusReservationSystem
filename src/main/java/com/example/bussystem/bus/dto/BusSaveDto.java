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

    private String lastStop;

    private String firstBus;

    private String lastBus;

    private MultipartFile busImage;

    private String categoryName;

    private Integer ticket_quantity;

    public Bus toEntity(Category category){
        return Bus.builder()
                .busNo(this.busNo)
                .firstBus(this.firstBus)
                .lastBus(this.lastBus)
                .firstStop(this.firstStop)
                .lastStop(this.lastStop)
                .category(category)
                .ticket_quantity(ticket_quantity)
                .build();
    }
}
