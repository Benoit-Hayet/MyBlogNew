package org.myblognew.MyBlogNew.Service;

import org.myblognew.MyBlogNew.dto.CategoryDTO;
import org.myblognew.MyBlogNew.mapper.CategoryMapper;
import org.myblognew.MyBlogNew.model.Category;
import org.myblognew.MyBlogNew.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
            return categories.stream().map(categoryMapper::convertToDTO).collect(Collectors.toList());
        }

    @GetMapping("/{id}")
    public Optional<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(categoryMapper.convertToDTO(optionalCategory.get()));
    }
}
