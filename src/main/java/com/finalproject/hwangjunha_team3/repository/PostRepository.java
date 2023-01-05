package com.finalproject.hwangjunha_team3.repository;

import com.finalproject.hwangjunha_team3.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAllByUserId(Integer id, Pageable pageable);
}
