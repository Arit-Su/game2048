package com.production.game2048.repository;

import com.production.game2048.model.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameStateRepository extends JpaRepository<GameState, Long> {

}