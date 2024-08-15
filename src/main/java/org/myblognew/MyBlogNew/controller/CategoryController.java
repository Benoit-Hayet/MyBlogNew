package org.myblognew.MyBlogNew.controller;

import org.myblognew.MyBlogNew.Service.CategoryService;
import org.myblognew.MyBlogNew.dto.ArticleDTO;
import org.myblognew.MyBlogNew.dto.CategoryDTO;
import org.myblognew.MyBlogNew.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.myblognew.MyBlogNew.repository.CategoryRepository;

import java.util.Optional;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Optional<CategoryDTO> updatedCategoryDTO = categoryService.updateCategory(id, categoryDTO);
        if (!updatedCategoryDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCategoryDTO.get());
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Category category = optionalCategory.get();
        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }

}