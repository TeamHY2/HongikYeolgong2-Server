package com.hongik.domain.library;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    Optional<Library> findFirstByOrderByIdAsc();
}
