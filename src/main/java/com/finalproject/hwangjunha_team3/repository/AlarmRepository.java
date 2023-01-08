package com.finalproject.hwangjunha_team3.repository;

import com.finalproject.hwangjunha_team3.domain.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {

    Page<Alarm> findAllByUserId(Integer id, Pageable pageable);
}
