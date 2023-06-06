package com.example.application.data.repository;

import com.example.application.data.entity.TUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TUser, Long> {
}
