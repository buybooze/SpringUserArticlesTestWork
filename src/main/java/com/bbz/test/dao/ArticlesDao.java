package com.bbz.test.dao;

import com.bbz.test.model.Article;
import com.bbz.test.service.IdenticonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Repository
public class ArticlesDao {
    private final Logger logger = LoggerFactory.getLogger(ArticlesDao.class);

    private RowMapper<Article> articleRowMapper = (rs, rowNum) ->
              new Article(
                    rs.getInt("id"),
                    rs.getString("author"),
                    rs.getString("title"),
                    new Scanner(rs.getClob("content").getAsciiStream(),"UTF-8").useDelimiter("\\A").next(),
                    rs.getLong("creationTimestamp"),
                    rs.getBlob("identicon").getBytes(1, (int) rs.getBlob("identicon").length()));


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IdenticonProvider identiconProvider;

    public List<Article> findAll() {
        logger.debug("findAll()");
        List<Article> articleList = jdbcTemplate.query(
                "SELECT id, author, title, content, creationTimestamp, identicon FROM articles",
                articleRowMapper);

        articleList.forEach(article -> logger.debug(article.toString()));
        return articleList;
    }

    public Article findById(int id) {
        logger.debug("findById("+id+")");
        List<Article> articleList = jdbcTemplate.query(
                "SELECT id, author, title, content, creationTimestamp, identicon FROM articles WHERE id = ?" , new Object[]{id},
                articleRowMapper);
        //queryForObject fell with exception, so this shit:
        if(articleList.isEmpty()) {
            logger.debug("article 404");
            return null;
        } else if(articleList.size() == 1) {
            logger.debug(articleList.get(0).toString());
            return articleList.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(articleList.size());
        }
    }

    public List<Article> findByTitle(String title) {
        logger.debug("findByTitle(" + title + ")");
        List<Article> articleList = jdbcTemplate.query(
                "SELECT id, author, title, content, creationTimestamp, identicon FROM articles WHERE LOWER(title) = ?", new Object[]{title.toLowerCase()},
                articleRowMapper);
        articleList.forEach(article -> logger.debug(article.toString()));
        return articleList;
    }

    public List<Article> findByTitleContains(String title) {
        logger.debug("findByTitleContains(" + title + ")");
        List<Article> articleList = jdbcTemplate.query(
                "SELECT id, author, title, content, creationTimestamp, identicon FROM articles WHERE LOWER(title) LIKE '%' || ? || '%'", new Object[]{title.toLowerCase()},
                articleRowMapper);
        articleList.forEach(article -> logger.debug(article.toString()));
        return articleList;
    }

    public List<Article> findByAuthor(String author) {
        logger.debug("findByAuthor(" + author + ")");
        List<Article> articleList = jdbcTemplate.query(
                "SELECT id, author, title, content, creationTimestamp, identicon FROM articles WHERE author = ?", new Object[]{author},
                articleRowMapper);
        articleList.forEach(article -> logger.debug(article.toString()));
        return articleList;
    }

    public List<Article> findByAuthorAndTitleContains(String author, String title) {
        logger.debug("findByAuthorAndTitleContains(" + author + ", " + title + ")");
        List<Article> articleList = jdbcTemplate.query(
                "SELECT id, author, title, content, creationTimestamp, identicon FROM articles WHERE LOWER(title) LIKE '%' || ? || '%' AND author = ?", new Object[]{title.toLowerCase(), author},
                articleRowMapper);
        articleList.forEach(article -> logger.debug(article.toString()));
        return articleList;
    }

    public void insert(Article article) {
        logger.debug("insert()");
        List<Article> articles = new ArrayList<>();
        articles.add(article);
        insertBatch(articles);
    }

    public void insertBatch(final List<Article> articles) {
        logger.debug("insertBatch()");
        articles.forEach(article -> logger.debug(article.toString()));
        String sql = "INSERT INTO articles(author, title, content, creationTimestamp, identicon) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Article article = articles.get(i);
                    ps.setString(1, article.getAuthor());
                    ps.setString(2, article.getTitle());
                    Reader reader = new StringReader(article.getContent());
                    ps.setClob(3, reader);
                    ps.setLong(4, article.getCreationTimestamp());
                    ps.setBlob(5, identiconProvider.getIdenticonInputStreamFromRest(article.getTitle()));
                }

                @Override
                public int getBatchSize() {
                    return articles.size();
                }
            });
        } catch (DuplicateKeyException e) {
            logger.debug("Tried violating UNIQUE CONSTRAINT " + e.getMessage());
        }
    }

    public void delete(int id) {
        logger.debug("delete(" + id + ")");
        jdbcTemplate.update("DELETE FROM articles WHERE id = ?", id);
    }
}