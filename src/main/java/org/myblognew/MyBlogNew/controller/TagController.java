package org.myblognew.MyBlogNew.controller;


import org.myblognew.MyBlogNew.Service.ArticleService;
import org.myblognew.MyBlogNew.Service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.myblognew.MyBlogNew.dto.TagDTO;



import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    public final TagService tagService;
    public final ArticleService articleService;

    public TagController(TagService tagService, ArticleService articleService) {
        this.tagService = tagService;
        this.articleService = articleService;
    }



    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        TagDTO savedTagDTO = tagService.createTag(tagDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTagDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        Optional<TagDTO> updateTagDTO = tagService.updateTag(id,tagDTO);
        if (!updateTagDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateTagDTO.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        if (tagService.deleteTag(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }


    }
}
