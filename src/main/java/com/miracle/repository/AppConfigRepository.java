package com.miracle.repository;

import com.miracle.model.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {
    Optional<AppConfig> findByAppId(String appId);
}