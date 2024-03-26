package com.woola.woola.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woola.woola.model.Comment;
import com.woola.woola.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public void save(Comment comment) {
		commentRepository.save(comment);
	}

	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	public Optional<Comment> findById(long id) {
		Optional<Comment> findById = commentRepository.findById(id);
		return findById;
	}

    public void deleteById(long id){
		commentRepository.deleteById(id);
	}
}