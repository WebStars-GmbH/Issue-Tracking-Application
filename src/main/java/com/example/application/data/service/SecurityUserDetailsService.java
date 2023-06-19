package com.example.application.data.service;

import com.example.application.data.entity.TUser;
import com.example.application.data.repository.TUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService  {
    @Autowired
    private TUserRepository tuserRepository;


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        TUser user = tuserRepository.findByUsername(username).get();
        System.out.println("retrieved user:" + user.getUsername() +
                user.getPassword());
        return new MyUserPrincipal(user, user.isActive());
    }
    public void createUser(TUser user) {

        tuserRepository.save((TUser) user);
    }
    public void deleteUser(TUser user) {

        tuserRepository.delete((TUser) user);
    }

}