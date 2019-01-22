package com.bbz.test.dao;

import com.bbz.test.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


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
                    Arrays.asList(rs.getString("roles").split(",")));

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

    public void insert(User user) {
        logger.debug("insert()");
        List<User> users = new ArrayList<>();
        users.add(user);
        insertBatch(users);
    }

    public void insertBatch(final List<User> users) {
        logger.debug("insertBatch()");
        users.forEach(user -> logger.debug(user.toString()));
        String sql = "INSERT INTO users(name, email, password, roles) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    User user = users.get(i);
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPassword());
                    ps.setString(4, String.join(",", user.getRoles()));
                }

                @Override
                public int getBatchSize() {
                    return users.size();
                }
            });
        } catch (DuplicateKeyException e) {
            logger.debug("Tried violating UNIQUE CONSTRAINT " + e.getMessage());
        }
    }

    public void delete(int id) {
        logger.debug("delete(" + id + ")");
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}