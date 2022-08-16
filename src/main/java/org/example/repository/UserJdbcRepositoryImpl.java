package org.example.repository;

import org.example.exception.NotFoundException;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserJdbcRepositoryImpl implements UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.username}")
    private String USERNAME;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Override
    public User save(User user) {
        String SQL = "INSERT INTO USERS (FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)" + "VALUES (?,?,?,?)";

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("USERS").usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    private User saveWithPreparedStatement(User user) {
        String SQL = "INSERT INTO USERS (FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)" + "VALUES (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SQL, new String[]{"id"});
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            return statement;
        }, keyHolder);

        user.setId((Long) keyHolder.getKey());
        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT ID, FIRST_NAME, LAST_NAME, CREATED_AT FROM USERS WHERE ID = ?",
                    new BeanPropertyRowMapper<>(User.class), id));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();

        String SQL = "SELECT * FROM USERS";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(SQL)) {
                while (resultSet.next()) {
                    User user = new User();

                    user.setId(resultSet.getLong("ID"));
                    user.setFirstName(resultSet.getString("FIRST_NAME"));
                    user.setLastName(resultSet.getString("LAST_NAME"));
                    user.setEmail(resultSet.getString("EMAIL"));
                    user.setPassword(resultSet.getString("PASSWORD"));
                    user.setCreatedAt(resultSet.getTimestamp("CREATED_AT").toLocalDateTime());

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void deleteUserById(Long id) {
        int status = jdbcTemplate.update("DELETE FROM USERS WHERE ID = ?", id);
        if (status == 0) {
            throw new NotFoundException("User with id " + id + " does not exist!");
        }
    }
}


