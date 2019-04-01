package com.spring.db.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class KeyDAOImpl implements KeyDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private KeyMapper keyMapper = new KeyMapper();

    public Key getKeyById(Long id) {
        String SQL_FIND_LOCATION = "select * from key where id = ?";
        return jdbcTemplate.queryForObject(SQL_FIND_LOCATION, new Object[]{id}, keyMapper);
    }

    public List<Key> getAllKeys() {
        String SQL_GET_ALL = "select * from key";
        return jdbcTemplate.query(SQL_GET_ALL, keyMapper);
    }

    public boolean createKey(Key key) {
        String SQL_INSERT_LOCATION = "insert into key(key) values(?)";
        return jdbcTemplate.update(SQL_INSERT_LOCATION, key.getKey()) > 0;
    }

    public boolean updateKey(Key key) {
        String SQL_UPDATE_LOCATION = "update key set key = ? where id = ?";
        return jdbcTemplate.update(SQL_UPDATE_LOCATION, key.getKey(), key.getId()) > 0;
    }

    public boolean deleteKey(Key key) {
        String SQL_DELETE_LOCATION = "delete from kry where id = ?";
        return jdbcTemplate.update(SQL_DELETE_LOCATION, key.getId()) > 0;
    }
}