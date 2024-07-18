/* Résumé : Permet à l'application de gérer les requêtes HTTP et d'effectuer des opérations CRUD sur l'entité Article,
Création d'un contrôleur REST.*/

package org.myblognew.MyBlogNew.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.myblognew.MyBlogNew.model.Article;
import org.myblognew.MyBlogNew.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController //@RestController : Indique que cette classe servira de contrôleur REST, capable de gérer les requêtes HTTP.
@RequestMapping("/articles") //@RequestMapping("/articles") : Spécifie que toutes les requêtes à ce contrôleur seront mappées à l'URL /articles.
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
/*@GetMapping : Indique que cette méthode doit gérer les requêtes HTTP GET à l'URL /articles.

La méthode public ResponseEntity<List<Article>> getAllArticles() est définie pour retourner un ResponseEntity contenant
une liste d'objets Article.

articles.isEmpty() vérifie si la liste des articles est vide et retourne une réponse HTTP 204 No Content en utilisant ResponseEntity.
noContent().build() le cas échéant qui signifie que la requête a été traitée avec succès, mais qu'aucun contenu n'est renvoyé.

return ResponseEntity.ok(articles); : Si la liste des articles n'est pas vide, une réponse HTTP 200 OK est retournée avec la liste
des articles dans le corps de la réponse.*/

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {

        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Article article = optionalArticle.get();
        return ResponseEntity.ok(article);
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
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {

        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Article article = optionalArticle.get();
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());
        Article updatedArticle = articleRepository.save(article);
        return ResponseEntity.ok(updatedArticle);

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
