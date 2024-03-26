package com.woola.woola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.woola.woola.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    
}