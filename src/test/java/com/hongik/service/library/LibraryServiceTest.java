package com.hongik.service.library;

import com.hongik.domain.library.Library;
import com.hongik.domain.library.LibraryRepository;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.library.response.LibraryResponse;
import org.assertj.core.api.Assertions;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LibraryServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private LibraryRepository libraryRepository;

    @AfterEach
    void tearDown() {
        libraryRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("Library 테이블에 데이터를 하나 초과해서 넣을 수 없다. 무조건 데이터 하나만 들어갈 수 있다.")
    @Test
    void createLibraryHoursWithDuplication() {
        // given
        Library library1 = createLibrary(4);
        libraryRepository.save(library1);

        // when // then
        Library library2 = createLibrary(6);
        assertThatThrownBy(() -> libraryRepository.save(library2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("열람실 이용 시간(4시간 or 6시간)을 조회한다.")
    @Test
    void getLibraryHours() {
        // given
        Library library = createLibrary(4);
        libraryRepository.save(library);

        // when
        LibraryResponse libraryHours = libraryService.getLibraryHours();

        // then
        assertThat(libraryHours.getId()).isNotNull();
        assertThat(libraryHours.getLibraryHours()).isEqualTo(4);
    }

    @DisplayName("열람실 이용 시간(4시간 or 6시간)을 수정한다.")
    @Test
    void updateLibraryHours() {
        // given
        Library library = createLibrary(4);
        libraryRepository.save(library);

        // when
        LibraryResponse libraryHours = libraryService.updateLibraryHours(6);

        // then
        assertThat(libraryHours.getId()).isNotNull();
        assertThat(libraryHours.getLibraryHours()).isEqualTo(6);
    }

    private Library createLibrary(final int libraryHours) {
        return Library.builder()
                .libraryHours(libraryHours)
                .build();
    }
}