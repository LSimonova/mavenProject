package org.example.controller;

import org.example.model.Article;
import org.example.service.ArticleService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "articles")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> getArticleById(@PathVariable Long articleId) {
        Optional<Article> articleOptional = articleService.getArticleById(articleId);
        if (articleOptional.isPresent()) {
            return new ResponseEntity<>(articleOptional.get(), HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article with id " + articleId + " does not exist");
    }

    /*@PostMapping
    public ResponseEntity<Article> saveArticle(@RequestBody Article article) {
        Article savedArticle = articleService.saveArticle(article);
        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
    }*/

    @PostMapping("/users/{userId}/articles")
    public ResponseEntity<Article> saveArticle(@PathVariable (value = "userId") Long userId,
                                               @RequestBody Article article) {
        Article article1 = userService.getUserById(userId).map(author -> {
            article.setAuthor(author);
            return articleService.saveArticle(article);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " does not exist"));
        return new ResponseEntity<>(article1, HttpStatus.CREATED);
    }


    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticleById(@PathVariable Long articleId) {
        articleService.deleteArticleById(articleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
