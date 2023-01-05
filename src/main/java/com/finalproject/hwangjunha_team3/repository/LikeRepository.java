package com.finalproject.hwangjunha_team3.repository;

import com.finalproject.hwangjunha_team3.domain.Like;
import com.finalproject.hwangjunha_team3.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findByPostIdAndUserId(Integer postId, Integer userId);

    Integer countAllByPost(Post post);
}
