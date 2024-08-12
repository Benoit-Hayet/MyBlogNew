package org.myblognew.MyBlogNew.controller;

import org.myblognew.MyBlogNew.dto.CategoryDTO;
import org.myblognew.MyBlogNew.dto.ArticleDTO;
import org.myblognew.MyBlogNew.model.Category;
import org.myblognew.MyBlogNew.model.Article;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.myblognew.MyBlogNew.repository.CategoryRepository;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<CategoryDTO> categoryDTOs = categories.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Category category = optionalCategory.get();
        return ResponseEntity.ok(convertToDTO(category));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(201).body(convertToDTO(savedCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Category category = optionalCategory.get();
        category.setName(categoryDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
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