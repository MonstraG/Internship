package com.spring.db.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Transactional
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UserMapper userMapper = new UserMapper();

    public User getUserByUsername(String username) {
        String SQL_FIND_KEY = "select users.username, password, enabled, role, marker_amount from users, settings " +
                "where users.username = settings.username and users.username = ?";
        return jdbcTemplate.query(SQL_FIND_KEY, new Object[]{username}, new ResultSetExtractor<User>() {
            public User extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next())
                    return userMapper.mapRow(resultSet, 1);
                return null;
            }
        });
    }

    public boolean userExists(String username) {
        String SQL_USER_EXISTS = "select count(*) from users where username = ?";
        Boolean result = jdbcTemplate.queryForObject(SQL_USER_EXISTS, new Object[]{username}, Boolean.class);
        if (result == null) {
            return false;
        }
        return result;
    }

    public void createUser(User user) {
        String SQL_CREATE_USER = "insert into users(username, password, enabled, role) values(?, ?, ?, ?)";
        jdbcTemplate.update(SQL_CREATE_USER, user.getUsername(), user.getPassword(), user.isEnabled(), user.getRole());
        String SQL_CREATE_USER_DEFAULT_SETTINGS = "insert into settings(username, marker_amount) values (?, ?)";
        jdbcTemplate.update(SQL_CREATE_USER_DEFAULT_SETTINGS, user.getUsername(), 100);
    }
}
