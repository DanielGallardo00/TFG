package com.woola.woola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.woola.woola.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
    

    @Transactional
    @Modifying
    @Query("DELETE FROM Location l WHERE l.route.id = :routeId")
    void deleteByRouteId(Long routeId);
}
