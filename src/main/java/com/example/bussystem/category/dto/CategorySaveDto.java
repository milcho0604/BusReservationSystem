package com.example.bussystem.category.dto;


import com.example.bussystem.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorySaveDto {
    private String categoryName;

    public Category toEntity(){
        return Category.builder()
                .categoryName(this.categoryName)
                .build();
    }
}
