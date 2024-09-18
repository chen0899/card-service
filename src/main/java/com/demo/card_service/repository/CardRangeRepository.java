package com.demo.card_service.repository;

import com.demo.card_service.model.CardRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRangeRepository  extends JpaRepository<CardRange, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM card_ranges WHERE :card BETWEEN min_range AND max_range")
    Optional<CardRange> findByCard(String card);
}
