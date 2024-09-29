package com.hongik.dto.wisesaying.request;

import com.hongik.domain.wisesaying.WiseSaying;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WiseSayingCreateRequest {

    @Schema(example = "현재의 작은 행동이 미래를 바꾼다.")
    @NotBlank(message = "명언은 필수입니다.")
    private String quote;

    @Schema(example = "알버트 아인슈타인")
    @NotBlank(message = "저자는 필수입니다.")
    private String author;

    @Builder
    public WiseSayingCreateRequest(final String quote, final String author) {
        this.quote = quote;
        this.author = author;
    }

    public WiseSaying toEntity() {
        return WiseSaying.builder()
                .quote(quote)
                .author(author)
                .build();
    }
}
