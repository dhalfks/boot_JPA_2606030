package com.example.boot.repository;

import com.example.boot.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<entity, id type>
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
