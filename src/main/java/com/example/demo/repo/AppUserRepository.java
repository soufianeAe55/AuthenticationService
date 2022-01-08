package com.example.demo.repo;

import com.example.demo.entite.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository <AppUser, Long> {
    AppUser findByUsername(String username);
}
