package com.hongik.service.wisesaying;

import com.hongik.domain.wisesaying.WiseSaying;
import com.hongik.domain.wisesaying.WiseSayingRepository;
import com.hongik.dto.wisesaying.request.WiseSayingCreateRequest;
import com.hongik.dto.wisesaying.response.WiseSayingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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
}
