package com.woola.woola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.woola.woola.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.route.id = :routeId")
    void deleteByRouteId(Long routeId);
}