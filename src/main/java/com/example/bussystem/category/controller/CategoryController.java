package com.example.bussystem.category.controller;


import com.example.bussystem.category.domain.Category;
import com.example.bussystem.category.dto.CategoryListDto;
import com.example.bussystem.category.dto.CategorySaveDto;
import com.example.bussystem.category.service.CategoryService;
import com.example.bussystem.common.dto.CommonErrorDto;
import com.example.bussystem.common.dto.CommonResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("create")
    public ResponseEntity<?> categoryCreatePost(@ModelAttribute CategorySaveDto dto){
        try {
            Category category = categoryService.categoryCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "카테고리등록에 성공하였습니다.", category.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> category(Pageable pageable){
        Page<CategoryListDto>  categoryListDtos= categoryService.categoryList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "카테고리 목록을 조회합니다.", categoryListDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}
