package com.hardtech.app.repositories;

import com.hardtech.app.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByProject_IdAndNameLike(Long id, String name);

}