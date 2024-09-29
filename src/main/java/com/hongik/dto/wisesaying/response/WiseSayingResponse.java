package com.hongik.dto.wisesaying.response;

import com.hongik.domain.wisesaying.WiseSaying;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WiseSayingResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "현재의 작은 행동이 미래를 바꾼다.")
    private String quote;

    @Schema(example = "알버트 아인슈타인")
    private String author;

    @Builder
    public WiseSayingResponse(final Long id, final String quote, final String author) {
        this.id = id;
        this.quote = quote;
        this.author = author;
    }

    public static WiseSayingResponse of(WiseSaying wiseSaying) {
        return WiseSayingResponse.builder()
                .id(wiseSaying.getId())
                .quote(wiseSaying.getQuote())
                .author(wiseSaying.getAuthor())
                .build();
    }
}
