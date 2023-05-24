package com.myfirstblog.repositories;

import com.myfirstblog.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(long postId);// Custom method// I am finding comment based on post_id this column is in comment table
    List<Comment> findByEmail(String email);
}
