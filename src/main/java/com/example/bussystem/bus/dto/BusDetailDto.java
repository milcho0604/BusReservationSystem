package com.example.bussystem.bus.dto;

import com.example.bussystem.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusDetailDto {

    private Long id;

    private String busNo;

    private String firstStop;

    private String stopover;

    private String lastStop;

    private Integer stopoverCount;

    private String firstBus;

    private String lastBus;

    private String imagePath;

    private String categoryName;

    private Long price;

    private String schedule;

    private Integer ticket_quantity;
}
