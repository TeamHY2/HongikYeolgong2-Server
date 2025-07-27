package com.hongik.domain.user;

import io.jsonwebtoken.Claims;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByPassword(String password);

    Optional<User> findBySub(String sub);

    List<User> findAllBySubIsNotNullAndSocialPlatform(SocialPlatform socialPlatform);

    // 특정 socialPlatform, sub 중복된 sub 전체를 가져오기 (sub별 중복건 조회용)
    @Query("SELECT u.sub "
            + "FROM User u "
            + "WHERE u.socialPlatform = :platform "
            + "AND u.sub IS NOT NULL "
            + "AND u.sub != '' "
            + "GROUP BY u.sub "
            + "HAVING COUNT(u) > 1")
    List<String> findDuplicateSubsBySocialPlatform(@Param("platform") SocialPlatform platform);

    @Query("SELECT u "
            + "FROM User u "
            + "WHERE u.socialPlatform = :platform "
            + "AND u.sub = :sub "
            + "AND u.sub IS NOT NULL "
            + "AND u.sub != ''")
    List<User> findBySocialPlatformAndSub(@Param("platform") SocialPlatform platform, @Param("sub") String sub);
}
