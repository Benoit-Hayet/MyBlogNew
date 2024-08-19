package org.myblognew.MyBlogNew.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.myblognew.MyBlogNew.dto.TagDTO;
import org.myblognew.MyBlogNew.model.Tag;
import org.myblognew.MyBlogNew.model.Article;
import org.myblognew.MyBlogNew.repository.TagRepository;
import org.myblognew.MyBlogNew.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    public TagController(TagRepository tagRepository, ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
    }



    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        Tag tag = convertToEntity(tagDTO);
        Tag savedTag = tagRepository.save(tag);
        return ResponseEntity.status(201).body(convertToDTO(savedTag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Tag tag = optionalTag.get();
        tag.setName(tagDTO.getName());
        Tag updatedTag = tagRepository.save(tag);
        return ResponseEntity.ok(convertToDTO(updatedTag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Tag tag = optionalTag.get();
        tagRepository.delete(tag);
        return ResponseEntity.noContent().build();
    }


}
