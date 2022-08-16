package org.example.service;

import org.example.exception.NotFoundException;
import org.example.model.User;
import org.example.repository.UserJdbcRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserJdbcRepository userJdbcRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserJdbcRepository userJdbcRepository) {
        this.userRepository = userRepository;
        this.userJdbcRepository = userJdbcRepository;
    }


    public List<User> getAllUsers() {
       //return userJdbcRepository.findAllUsers();
        return userRepository.findAll();
    }

    //public User getUserById(Long userId) {
        //return userRepository.findById(userId).orElseThrow(() ->
               //new IllegalStateException("User with id " + userId + " does not exist!"));
   // }

    //public Optional<User> getUserById(Long userId) {
        //return userJdbcRepository.findUserById(userId);

    //}

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


    public User saveUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    //public void deleteUserById(Long userId) {
        //userJdbcRepository.deleteUserById(userId);
    //}

    public void deleteUserById(Long userId) {
        boolean isExist = userRepository.existsById(userId);
        if (!isExist) {
            throw new NotFoundException("User with id " + userId + " does not exist!");
       }
        userRepository.deleteById(userId);
    }
}
