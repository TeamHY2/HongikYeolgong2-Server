package com.hongik.service.wisesaying;

import com.hongik.domain.wisesaying.WiseSaying;
import com.hongik.domain.wisesaying.WiseSayingRepository;
import com.hongik.dto.wisesaying.request.WiseSayingCreateRequest;
import com.hongik.dto.wisesaying.response.WiseSayingResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WiseSayingServiceTest {

    @Autowired
    private WiseSayingService wiseSayingService;

    @Autowired
    private WiseSayingRepository wiseSayingRepository;

    @AfterEach
    void tearDown() {
        wiseSayingRepository.deleteAllInBatch();
    }

    @DisplayName("명언을 List 형식으로 저장한다.")
    @Test
    void createWiseSaying() {
        // given
        WiseSayingCreateRequest request = WiseSayingCreateRequest.builder()
                .quote("말")
                .author("저자")
                .build();

        // when
        List<WiseSayingResponse> wiseSayingResponse = wiseSayingService.createWiseSaying(List.of(request));

        // then
        assertThat(wiseSayingResponse).hasSize(1)
                .extracting("quote", "author")
                .containsExactlyInAnyOrder(
                        tuple("말", "저자")
                );
    }

    @DisplayName("오늘날짜 기준으로 toEpochDay를 사용하여 일수를 반환하고 나머지 연산을 통해 인덱스를 추출하여 명언을 조회합니다.")
    @Test
    void getWiseSaying() {
        // given
        // 현재 날짜 % 서버에 저장된 명언 개수로 인덱스를 추출한다.
        // 2024-09-29 기준 toEpochDay 값은 19995, 명언은 3개이기 때문에 19995 % 3 = 0
        // 2024-09-30 기준 toEpochDay 값은 19996, 명언은 3개이기 때문에 19996 % 3 = 1
        // 2024-10-01 기준 toEpochDay 값은 19997, 명언은 3개이기 때문에 19997 % 3 = 2
        // 2024-10-02 기준 toEpochDay 값은 19998, 명언은 3개이기 때문에 19998 % 3 = 0 다시 0으로 돌아온다.
        WiseSaying wiseSaying1 = createWiseSaying("quote1", "author1");
        WiseSaying wiseSaying2 = createWiseSaying("quote2", "author2");
        WiseSaying wiseSaying3 = createWiseSaying("quote3", "author3");
        wiseSayingRepository.saveAll(List.of(wiseSaying1, wiseSaying2, wiseSaying3));

        LocalDate now1 = LocalDate.of(2024, 9, 29);
        LocalDate now2 = LocalDate.of(2024, 9, 30);
        LocalDate now3 = LocalDate.of(2024, 10, 1);
        LocalDate now4 = LocalDate.of(2024, 10, 2);

        // when
        WiseSayingResponse wiseSayingResponse1 = wiseSayingService.getWiseSaying(now1);
        WiseSayingResponse wiseSayingResponse2 = wiseSayingService.getWiseSaying(now2);
        WiseSayingResponse wiseSayingResponse3 = wiseSayingService.getWiseSaying(now3);
        WiseSayingResponse wiseSayingResponse4 = wiseSayingService.getWiseSaying(now4);

        // then
        assertThat(wiseSayingResponse1)
                .extracting("quote", "author")
                .containsExactlyInAnyOrder("quote1", "author1");

        assertThat(wiseSayingResponse2)
                .extracting("quote", "author")
                .containsExactlyInAnyOrder("quote2", "author2");

        assertThat(wiseSayingResponse3)
                .extracting("quote", "author")
                .containsExactlyInAnyOrder("quote3", "author3");

        assertThat(wiseSayingResponse4)
                .extracting("quote", "author")
                .containsExactlyInAnyOrder("quote1", "author1");
    }


    private WiseSaying createWiseSaying(String quote, String author) {
        return WiseSaying.builder()
                .author(author)
                .quote(quote)
                .build();
    }
}