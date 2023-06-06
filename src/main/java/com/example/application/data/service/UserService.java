package com.example.application.data.service;


import com.example.application.data.entity.TUser;
import com.example.application.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<TUser> findAllUsers() {
        return userRepository.findAll();
    }

    public TUser findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public TUser saveUser(TUser user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

