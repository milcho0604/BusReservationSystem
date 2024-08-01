package com.example.bussystem.category.service;


import com.example.bussystem.category.domain.Category;
import com.example.bussystem.category.dto.CategoryListDto;
import com.example.bussystem.category.dto.CategorySaveDto;
import com.example.bussystem.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category categoryCreate(CategorySaveDto dto){
        if (categoryRepository.findByCategoryName(dto.getCategoryName()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
        Category category = categoryRepository.save(dto.toEntity());
        return category;
    }

    public Page<CategoryListDto> categoryList(Pageable pageable){
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(a -> a.listFromEntity());
    }
}
