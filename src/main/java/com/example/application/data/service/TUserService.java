package com.example.application.data.service;


import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Team;
import com.example.application.data.repository.TUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class TUserService {
    private final TUserRepository tUserRepository;

    @Autowired
    public TUserService(TUserRepository tUserRepository) {
        this.tUserRepository = tUserRepository;
    }

    public List<TUser> findAllUsers() {
        return tUserRepository.findAll();
    }

    public List<TUser> findAllActiveUsers() { return tUserRepository.findTUsersActive(); }

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

/*
    // Suche nach in einem TUser entweder oder (firstname, lastname, username, email, role, websites)
    public List<TUser> findUsersBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return tUserRepository.findAll();
        } else {
            return tUserRepository.searchBySearchTerm(searchTerm);
        }
    }
*/

    // search in firstname, lastname, username, email, role or websites for ACTIVE users
    public List<TUser> findActiveUsersBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return tUserRepository.findTUsersActive();
        } else {
            return tUserRepository.searchActiveBySearchTerm(searchTerm);
        }
    }

    // search in firstname, lastname, username, email, role or websites for ALL users (active + inactive)
    public List<TUser> findAllUsersBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return tUserRepository.findAll();
        } else {
            return tUserRepository.searchAllBySearchTerm(searchTerm);
        }
    }


}
