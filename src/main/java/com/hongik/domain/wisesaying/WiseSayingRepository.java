package com.hongik.domain.wisesaying;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WiseSayingRepository extends JpaRepository<WiseSaying, Long> {

    // 인덱스 기반으로 명언 조회
    @Query(value = "select * " +
            "from wise_saying ws " +
            "limit 1 " +
            "offset :index", nativeQuery = true)
    WiseSaying findWiseSayingByIndex(@Param("index") int index);
}
