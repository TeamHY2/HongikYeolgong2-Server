package com.hongik.domain.user;

import io.jsonwebtoken.Claims;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByPassword(String password);

    Optional<User> findBySub(String sub);
}
