package com.woola.woola.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woola.woola.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
    
}
