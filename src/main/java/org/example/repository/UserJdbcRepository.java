package org.example.repository;

import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserJdbcRepository {

    User save(User user);

    Optional<User> findUserById(Long id);

    List<User> findAllUsers();

    void deleteUserById(Long id);
}
