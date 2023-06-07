package com.example.application.data.service;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class WebsiteService {
    private final WebsiteRepository websiteRepository;

    @Autowired
    public WebsiteService(WebsiteRepository websiteRepository) {
        this.websiteRepository = websiteRepository;
    }

    public List<Website> getAllWebsites() {
        return websiteRepository.findAll();
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
}
