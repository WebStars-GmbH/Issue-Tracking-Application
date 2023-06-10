package com.example.application.data.service;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class WebsiteService {
    private final WebsiteRepository websiteRepository;
    private final TUserRepository userRepository; // Hinzufügen der UserRepository-Referenz

    @Autowired
    public WebsiteService(WebsiteRepository websiteRepository, TUserRepository userRepository) {
        this.websiteRepository = websiteRepository;
        this.userRepository = userRepository;
    }

    public List<Website> getAllWebsites() {
        return websiteRepository.findAll();
    }
    public List<Website> getAllWebsitesByUser(TUser user) {
        return websiteRepository.findByUser(user);
    }

    public Website findWebsiteById(Long id) {
        return websiteRepository.findById(id).orElse(null);
    }

    public Website saveWebsite(Website website) {
        return websiteRepository.save(website);
    }

    public void deleteWebsite(Long id) {
        websiteRepository.deleteById(id);
    }

    public void deleteWebsitesByUser(TUser user) {
        List<Website> websitesToDelete = websiteRepository.findByUser(user);
        if (!websitesToDelete.isEmpty()) {
            for (Website website : websitesToDelete) {
                website.setUser(null);
                websiteRepository.delete(website);
            }
            userRepository.save(user); // Speichern der TUser-Instanz
        }
    }
}