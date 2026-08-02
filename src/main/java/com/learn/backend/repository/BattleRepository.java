package com.learn.backend.repository;

import com.learn.backend.entity.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BattleRepository extends JpaRepository<Battle, UUID> {
}
