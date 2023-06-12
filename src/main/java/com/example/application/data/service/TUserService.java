package com.example.application.data.service;


import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Team;
import com.example.application.data.repository.TUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TUserService {
    private final TUserRepository tUserRepository;

    @Autowired
    public TUserService(TUserRepository tUserRepository) {
        this.tUserRepository = tUserRepository;
    }

    public List<TUser> findAllUsers() {
        return tUserRepository.findAll();
    }

    public TUser findUserById(Long id) {
        return tUserRepository.findById(id).orElse(null);
    }

    public TUser findUserByUsername(String username) {
        return tUserRepository.getTUserByUsername(username);
    }
    public List<TUser> findUsersByTeam(Team team) {
        return tUserRepository.getTUsersByTeam(team);
    }

    public List<TUser> findAllTUsersByRole(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return tUserRepository.findAll();
        } else {
            return tUserRepository.searchByRole(stringFilter);
        }
    }

    public void saveUser(TUser user) {
        tUserRepository.save(user);
    }

    public void deleteUser(Long id) {
        tUserRepository.deleteById(id);
    }
}

