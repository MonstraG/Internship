package com.spring.db.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class KeyDAOImpl implements KeyDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private KeyMapper keyMapper = new KeyMapper();

    public Key getKeyById(Long id) {
        String SQL_FIND_KEY = "select * from keys where id = ?";
        return jdbcTemplate.queryForObject(SQL_FIND_KEY, new Object[]{id}, keyMapper);
    }

    public Key getKeyByValue(String key) {
        String SQL_FIND_KEY = "select * from keys where key = ?";
        return jdbcTemplate.query(SQL_FIND_KEY, new Object[]{key}, new ResultSetExtractor<Key>() {
            public Key extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next())
                    return keyMapper.mapRow(resultSet, 1);
                return null;
            }
        });
    }

    public boolean keyExists(String key) {
        String SQL_KEY_COUNT = "select count(*) from keys where key = ?";
        Boolean result = jdbcTemplate.queryForObject(SQL_KEY_COUNT,  new Object[] { key }, Boolean.class);
        if (result == null) { return false; }
        return result;
    }

    public List<Key> getAllKeys() {
        String SQL_GET_ALL = "select * from keys";
        return jdbcTemplate.query(SQL_GET_ALL, keyMapper);
    }

    public boolean createKey(Key key) {
        String SQL_INSERT_KEY = "insert into keys(key) values(?) ON CONFLICT DO NOTHING";
        return jdbcTemplate.update(SQL_INSERT_KEY, key.getKey()) > 0;
    }

    public boolean updateKey(Key key) {
        String SQL_UPDATE_KEY = "update keys set key = ? where id = ?";
        return jdbcTemplate.update(SQL_UPDATE_KEY, key.getKey(), key.getId()) > 0;
    }

    public boolean deleteKey(Key key) {
        String SQL_DELETE_KEY = "delete from keys where id = ?";
        return jdbcTemplate.update(SQL_DELETE_KEY, key.getId()) > 0;
    }
}