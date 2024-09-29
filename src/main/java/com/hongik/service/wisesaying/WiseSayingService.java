package com.hongik.service.wisesaying;

import com.hongik.domain.wisesaying.WiseSaying;
import com.hongik.domain.wisesaying.WiseSayingRepository;
import com.hongik.dto.wisesaying.request.WiseSayingCreateRequest;
import com.hongik.dto.wisesaying.response.WiseSayingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WiseSayingService {

    private final WiseSayingRepository wiseSayingRepository;

    @Transactional
    public List<WiseSayingResponse> createWiseSaying(List<WiseSayingCreateRequest> request) {
        List<WiseSaying> wiseSayings = request.stream()
                .map(WiseSayingCreateRequest::toEntity)
                .toList();

        wiseSayingRepository.saveAll(wiseSayings);

        return wiseSayings.stream()
                .map(WiseSayingResponse::of)
                .toList();
    }

    public WiseSayingResponse getWiseSaying(LocalDate now) {
        // 총 명언 개수
        long wiseSayingTotalCount = wiseSayingRepository.count();

        // 현재 날짜 기준 인덱스 계산
        int index = (int) (now.toEpochDay() % wiseSayingTotalCount);

        WiseSaying wiseSaying = wiseSayingRepository.findWiseSayingByIndex(index);
        return WiseSayingResponse.of(wiseSaying);
    }
}
