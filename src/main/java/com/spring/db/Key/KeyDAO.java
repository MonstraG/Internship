package com.spring.db.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class KeyDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private KeyMapper keyMapper = new KeyMapper();

    public List<Key> getAllKeysByUsername(String username) {
        String SQL_GET_ALL = "select * from keys where username = ?";
        return jdbcTemplate.query(SQL_GET_ALL, new Object[]{username}, keyMapper);
    }

    public boolean keyExists(String key) {
        String SQL_KEY_EXISTS = "select count(*) from keys where key = ?";
        Boolean result = jdbcTemplate.queryForObject(SQL_KEY_EXISTS, new Object[]{key}, Boolean.class);
        if (result == null) {
            return false;
        }
        return result;
    }

    public void createKey(Key key) {
        String SQL_INSERT_KEY = "insert into keys(username, key) values(?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(SQL_INSERT_KEY, key.getUsername(), key.getKey());
    }
}