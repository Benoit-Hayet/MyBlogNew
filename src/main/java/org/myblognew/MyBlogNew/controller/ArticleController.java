/* Résumé : Permet à l'application de gérer les requêtes HTTP et d'effectuer des opérations CRUD sur l'entité Article,
Création d'un contrôleur REST.*/

package org.myblognew.MyBlogNew.controller;

import org.myblognew.MyBlogNew.dto.ArticleDTO;
import org.myblognew.MyBlogNew.model.Category;
import org.myblognew.MyBlogNew.model.Tag;
import org.myblognew.MyBlogNew.repository.CategoryRepository;
import org.myblognew.MyBlogNew.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.myblognew.MyBlogNew.model.Article;
import org.myblognew.MyBlogNew.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController //@RestController : Indique que cette classe servira de contrôleur REST, capable de gérer les requêtes HTTP.
@RequestMapping("/articles") //@RequestMapping("/articles") : Spécifie que toutes les requêtes à ce contrôleur seront mappées à l'URL /articles.
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    private ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setCreatedAt(article.getCreatedAt());
        articleDTO.setUpdatedAt(article.getUpdatedAt());
        if (article.getCategory() != null) {
            articleDTO.setCategoryId(article.getCategory().getId());
        }
        if (article.getTags() != null) {
            articleDTO.setTagIds(article.getTags().stream().map(Tag::getId).collect(Collectors.toList()));
        }
        return articleDTO;
    }
        private Article convertToEntity(ArticleDTO articleDTO) {
            Article article = new Article();
            article.setId(articleDTO.getId());
            article.setTitle(articleDTO.getTitle());
            article.setContent(articleDTO.getContent());
            article.setCreatedAt(articleDTO.getCreatedAt());
            article.setUpdatedAt(articleDTO.getUpdatedAt());
            if (articleDTO.getCategoryId() != null) {
                Category category = categoryRepository.findById(articleDTO.getCategoryId()).orElse(null);
                article.setCategory(category);
            }
            if (articleDTO.getTagIds() != null) {
                List<Tag> tags = tagRepository.findAllById(articleDTO.getTagIds());
                article.setTags(tags);
            }
            return article;
        }


    public ArticleController(ArticleRepository articleRepository ,CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }
/*@GetMapping : Indique que cette méthode doit gérer les requêtes HTTP GET à l'URL /articles.

La méthode public ResponseEntity<List<Article>> getAllArticles() est définie pour retourner un ResponseEntity contenant
une liste d'objets Article.

articles.isEmpty() vérifie si la liste des articles est vide et retourne une réponse HTTP 204 No Content en utilisant ResponseEntity.
noContent().build() le cas échéant qui signifie que la requête a été traitée avec succès, mais qu'aucun contenu n'est renvoyé.

return ResponseEntity.ok(articles); : Si la liste des articles n'est pas vide, une réponse HTTP 200 OK est retournée avec la liste
des articles dans le corps de la réponse.*/

    @GetMapping
    /*public ResponseEntity<List<Article>> getAllArticles() {
    Mise a jour de la méthode pour mise en place du DTO  */
        public ResponseEntity<List<ArticleDTO>> getAllArticles() {

            List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        /*return ResponseEntity.ok(articles);
        Mise a jour de la réponse en DTO

        La méthode getAllArticles() convertit chaque objet Article récupéré en un objet ArticleDTO.

articles.stream() convertit la liste d'articles en un flux (stream) de données.
.map(this::convertToDTO) applique la méthode convertToDTO à chaque article dans le flux, transformant chaque Article en ArticleDTO.
.collect(Collectors.toList()) collecte les résultats du flux dans une nouvelle liste de ArticleDTO.
Récupère à nouveau tes articles via Postman : la boucle infinie ne se produit plus et tu obtiens désormais les données
correctement sérialisées, conformément aux champs définis dans ArticleDTO.*/
        List<ArticleDTO> articleDTOs = articles.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(articleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Article article = optionalArticle.get();
        return ResponseEntity.ok(convertToDTO(article));
    }

/*http://localhost:8080/articles/search-title?searchTerms=First%20Post*/
    @GetMapping("/search-title")
    public ResponseEntity<List<Article>> getArticlesByTitle(@RequestParam String searchTerms) {
        List<Article> articles = articleRepository.findByTitle(searchTerms);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }
    @GetMapping("/articles-date/{createdAt}")
    public ResponseEntity<List<Article>> getArticlesByCreatedAt(@PathVariable LocalDateTime createdAt) {
        List<Article> articles = articleRepository.findByCreatedAt(createdAt);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Article>> getLatestArticles() {
        List<Article> articles = articleRepository.findTop5ByOrderByCreatedAtDesc();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Article>> getArticlesByContent(@PathVariable String keyword) {
        List<Article> articles = articleRepository.findByContentContaining(keyword);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/created-after/{createdAt}")
    public ResponseEntity<List<Article>> getArticlesCreatedAfter(@PathVariable String createdAt) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(createdAt);
            List<Article> articles = articleRepository.findByCreatedAtAfter(dateTime);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        Article article = convertToEntity(articleDTO);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedArticle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Article article = optionalArticle.get();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        // Mise à jour de la catégorie
        if (articleDTO.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(articleDTO.getCategoryId());
            if (!optionalCategory.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            Category category = optionalCategory.get();
            article.setCategory(category);
        }

        Article updatedArticle = articleRepository.save(article);
        return ResponseEntity.ok(convertToDTO(updatedArticle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {

        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Article article = optionalArticle.get();
        articleRepository.delete(article);
        return ResponseEntity.noContent().build();
    }


}
