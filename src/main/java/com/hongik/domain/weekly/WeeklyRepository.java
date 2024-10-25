package com.hongik.domain.weekly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WeeklyRepository extends JpaRepository<Weekly, Long> {

    List<Weekly> findAllByYears(@Param("year") int year);

    boolean existsByYears(@Param("year") int year);

    Optional<Weekly> findByWeekNumber(@Param("weekNumber") int weekNumber);
}
