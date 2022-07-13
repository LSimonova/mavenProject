package org.example.controller;

import org.example.model.Article;
import org.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
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

    @PostMapping
    public ResponseEntity<Article> saveArticle(@RequestBody Article article) {
        Article savedArticle = articleService.saveArticle(article);
        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticleById(@PathVariable Long articleId) {
        articleService.deleteArticleById(articleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
