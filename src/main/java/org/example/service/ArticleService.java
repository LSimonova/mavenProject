package org.example.service;

import org.example.exception.NotFoundException;
import org.example.model.Article;
import org.example.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long articleId) {
        return articleRepository.findById(articleId);
    }

    public Article saveArticle(Article article) {
        article.setCreatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    public void deleteArticleById(Long articleId) {
        boolean isExist = articleRepository.existsById(articleId);
        if (!isExist) {
            throw new NotFoundException("Article with id " + articleId + " does not exist");
        }
        articleRepository.deleteById(articleId);
    }
}
