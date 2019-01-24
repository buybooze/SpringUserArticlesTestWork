package com.bbz.test.dao;

import com.bbz.test.model.User;
import com.bbz.test.model.VerificationToken;
import com.bbz.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

@Repository
public class VerificationTokenDao {

    private final Logger logger = LoggerFactory.getLogger(VerificationTokenDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    private RowMapper<VerificationToken> tokenRowMapper = (rs, rowNum) ->
            new VerificationToken(
                    rs.getInt("id"),
                    rs.getString("token"),
                    userService.findUserById(rs.getInt("user_id")),
                    rs.getDate("expiry_date"));



    public VerificationToken findByToken(String token) {
        logger.debug("findByToken("+token+")");
        List<VerificationToken> tokenList = jdbcTemplate.query(
                "SELECT * FROM tokens WHERE token = ?" , new Object[]{token},
                tokenRowMapper);
        //queryForObject fell with exception, so this shit:
        if(tokenList.isEmpty()) {
            logger.debug("token 404");
            return null;
        } else if(tokenList.size() == 1) {
            logger.debug(tokenList.get(0).toString());
            return tokenList.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(tokenList.size());
        }
    }

    public VerificationToken findByUser(User user) {
        logger.debug("findByUser("+user+")");
        List<VerificationToken> tokenList = jdbcTemplate.query(
                "SELECT * FROM tokens WHERE user_id = ?" , new Object[]{user.getId()},
                tokenRowMapper);
        //queryForObject fell with exception, so this shit:
        if(tokenList.isEmpty()) {
            logger.debug("token 404");
            return null;
        } else if(tokenList.size() == 1) {
            logger.debug(tokenList.get(0).toString());
            return tokenList.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(tokenList.size());
        }
    }

    public VerificationToken findById(int id) {
        logger.debug("findById("+id+")");
        List<VerificationToken> tokenList = jdbcTemplate.query(
                "SELECT * FROM tokens WHERE id = ?" , new Object[]{id},
                tokenRowMapper);
        //queryForObject fell with exception, so this shit:
        if(tokenList.isEmpty()) {
            logger.debug("token 404");
            return null;
        } else if(tokenList.size() == 1) {
            logger.debug(tokenList.get(0).toString());
            return tokenList.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(tokenList.size());
        }
    }


    public VerificationToken save(VerificationToken token) {
        logger.debug("save()");
        String sql = "INSERT INTO tokens(token, user_id, expiry_date) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, token.getToken());
                ps.setInt(2, token.getUser().getId());
                logger.debug("before setting date");
                logger.debug("tokendate"+ token.getExpiryDate());
                logger.debug("tokendate"+ token.getExpiryDate().getClass().getSimpleName());
                //logger.debug("sqldate"+ Date.valueOf(String.valueOf(token.getExpiryDate())));
                //logger.debug("sqldate"+ Date.valueOf(String.valueOf(token.getExpiryDate())).getClass().getSimpleName());
                ps.setDate(3, token.getExpiryDate());
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            logger.debug("Tried violating UNIQUE CONSTRAINT " + e.getMessage());
            return null;
        } catch (Exception e) {
            logger.debug("failed saving " + e);
        }
        return findById(keyHolder.getKey().intValue());
    }
}
