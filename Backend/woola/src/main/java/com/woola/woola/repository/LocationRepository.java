package com.woola.woola.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woola.woola.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
    
}
