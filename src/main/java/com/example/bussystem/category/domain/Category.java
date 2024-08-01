package com.example.bussystem.category.domain;


import com.example.bussystem.category.dto.CategoryListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    public CategoryListDto listFromEntity(){
        return CategoryListDto.builder()
                .id(this.id)
                .categoryName(this.categoryName)
                .build();
    }
}
