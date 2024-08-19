package org.myblognew.MyBlogNew.Service;

import org.myblognew.MyBlogNew.dto.TagDTO;
import org.myblognew.MyBlogNew.model.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TagService {

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        if (tags.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<TagDTO> tagDTOs = tags.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tagDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Tag tag = optionalTag.get();
        return ResponseEntity.ok(convertToDTO(tag));
    }
}
