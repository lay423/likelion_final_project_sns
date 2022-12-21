package com.finalproject.hwangjunha_team3.repository;


import com.finalproject.hwangjunha_team3.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
