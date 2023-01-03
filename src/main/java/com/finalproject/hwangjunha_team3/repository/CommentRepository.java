package com.finalproject.hwangjunha_team3.repository;

import com.finalproject.hwangjunha_team3.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByPostId(Integer id, Pageable pageable);
}
