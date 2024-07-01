package com.woola.woola.repository;

import java.sql.Blob;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.woola.woola.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
    
    @Query(value = "SELECT * FROM route WHERE userId = :num", nativeQuery=true)
    List<Route> findAllByUser(@Param("num") Long num);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="UPDATE route SET name = :name, booking = :booking, description = :description, image = :image, lat = :lat, lng = :lng WHERE id = :id", nativeQuery = true)
    void updateRoute(@Param("id") long id, @Param("name") String name, @Param("booking") Boolean booking, @Param("description") String description, @Param("image") Blob image, @Param("lat") Float lat,@Param("lng") Float lng);
}
