package com.spring.db.Key;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KeyMapper implements RowMapper<Key> {

    public Key mapRow(ResultSet resultSet, int i) throws SQLException {
        Key key = new Key();
        key.setId(resultSet.getLong("id"));
        key.setKey(resultSet.getString("key"));
        return key;
    }
}
