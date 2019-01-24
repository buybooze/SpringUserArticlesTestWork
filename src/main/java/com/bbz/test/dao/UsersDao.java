package com.bbz.test.dao;

import com.bbz.test.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class UsersDao {
    private final Logger logger = LoggerFactory.getLogger(UsersDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> usersRowMapper = (rs, rowNum) ->
             new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    Arrays.asList(rs.getString("roles").split(",")),
                    rs.getBoolean("enabled"));

    public List<User> findAll() {
        logger.debug("findAll()");
        List<User> userList = jdbcTemplate.query(
                "SELECT * FROM users",
                usersRowMapper);
        userList.forEach(user -> logger.debug(user.toString()));
        return userList;
    }

    public User findByEmail(String email) {
        logger.debug("findByEmail("+email+")");
        List<User> userList = jdbcTemplate.query(
                "SELECT * FROM users WHERE email = ?" , new Object[]{email},
                usersRowMapper);
        //queryForObject fell with exception, so this shit:
        if(userList.isEmpty()) {
            logger.debug("user 404");
            return null;
        } else if(userList.size() == 1) {
            logger.debug(userList.get(0).toString());
            return userList.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(userList.size());
        }
    }

    public User findByName(String name) {
        logger.debug("findByName("+name+")");
        List<User> userList = jdbcTemplate.query(
                "SELECT * FROM users WHERE name = ?" , new Object[]{name},
                usersRowMapper);
        //queryForObject fell with exception, so this shit:
        if(userList.isEmpty()) {
            logger.debug("user 404");
            return null;
        } else if(userList.size() == 1) {
            logger.debug(userList.get(0).toString());
            return userList.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(userList.size());
        }
    }

    public User findById(int id) {
        logger.debug("findById(" + id + ")");
        List<User> userList = jdbcTemplate.query(
                "SELECT * FROM users WHERE id = ?", new Object[]{id},
                usersRowMapper);
        //queryForObject fell with exception, so this shit:
        if(userList.isEmpty()) {
            logger.debug("user 404");
            return null;
        } else if(userList.size() == 1) {
            logger.debug(userList.get(0).toString());
            return userList.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(userList.size());
        }
    }

    public User insert(User user) {
        logger.debug("insert()");
        String sql = "INSERT INTO users(name, email, password, roles, enabled) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(con ->  {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPassword());
                    ps.setString(4, String.join(",", user.getRoles()));
                    ps.setBoolean(5, user.isEnabled());
                    return ps;
                }, keyHolder);
        } catch (DuplicateKeyException e) {
            logger.debug("Tried violating UNIQUE CONSTRAINT " + e.getMessage());
            return null;
        }
        return findById(keyHolder.getKey().intValue());
    }



    public List<User> insertBatch(final List<User> users) {
        logger.debug("insertBatch()");
        List<User> userList = new ArrayList<>();
        for(User user : users) {
            userList.add(insert(user));
        }
        return userList;
    }

    public void delete(int id) {
        logger.debug("delete(" + id + ")");
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    public User update(User user) {
        logger.debug("update()");
        String sql = "UPDATE users SET name=?, email=?, password=?, roles=?, enabled=? WHERE id=?";
        try {
            jdbcTemplate.update(con ->  {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.setString(4, String.join(",", user.getRoles()));
                ps.setBoolean(5, user.isEnabled());
                ps.setInt(6,    user.getId());
                return ps;
            });
        } catch (DuplicateKeyException e) {
            logger.debug("Tried violating UNIQUE CONSTRAINT " + e.getMessage());
            return null;
        }
        return findById(user.getId());
    }

    //TODO PreparedStatementSetter to PreparedStatementCreator
}