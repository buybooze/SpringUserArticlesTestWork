package com.bbz.test.service;

import com.bbz.test.dao.ArticlesDao;
import com.bbz.test.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ArticlesService {

    @Autowired
    private ArticlesDao articlesDao;

    public void save(Article article) {
        articlesDao.insert(article);
    }

    public void saveBatch(List<Article> articleList) {
        articlesDao.insertBatch(articleList);
    }

    public void delete(int id) {
        articlesDao.delete(id);
    }

    public List<Article> findAll() {
        return sortByEditedDateTime(articlesDao.findAll());
    }

    public List<Article> findByTitle(String title) {
        return sortByEditedDateTime(articlesDao.findByTitle(title));
    }

    public List<Article> findByAuthor(String author) {
        return sortByEditedDateTime(articlesDao.findByAuthor(author));
    }

    public List<Article> findByTitleContains(String title) {
        return sortByEditedDateTime(articlesDao.findByTitleContains(title));
    }

    public List<Article> findByAuthorAndTitleContains(String author, String title) {
        if(!title.isEmpty() && !author.isEmpty()) {
            return sortByEditedDateTime(articlesDao.findByAuthorAndTitleContains(author, title));
        } else if(!title.isEmpty()) {
            return findByTitleContains(title);
        } else if(!author.isEmpty()) {
            return findByAuthor(author);
        } else {
            return findAll();
        }
    }

    public Article findById(int id) {
        return articlesDao.findById(id);
    }

    private List<Article> sortByEditedDateTime(List<Article> articleList) {
        return StreamSupport
                .stream(
                        Spliterators.spliteratorUnknownSize(articleList.iterator(), Spliterator.NONNULL),
                        false)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}
