package com.hongik.dto.wisesaying.response;

import com.hongik.domain.wisesaying.WiseSaying;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WiseSayingResponse {

    private Long id;

    private String quote;

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
