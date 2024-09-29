package com.hongik.service.wisesaying;

import com.hongik.domain.wisesaying.WiseSayingRepository;
import com.hongik.dto.wisesaying.request.WiseSayingCreateRequest;
import com.hongik.dto.wisesaying.response.WiseSayingResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WiseSayingServiceTest {

    @Autowired
    private WiseSayingService wiseSayingService;

    @Autowired
    private WiseSayingRepository wiseSayingRepository;

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
}