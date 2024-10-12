package com.hongik.dto.library.response;

import com.hongik.domain.library.Library;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LibraryResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "4")
    private int libraryHours;

    @Builder
    public LibraryResponse(final Long id, final int libraryHours) {
        this.id = id;
        this.libraryHours = libraryHours;
    }

    public static LibraryResponse of(Library library) {
        return LibraryResponse.builder()
                .id(library.getId())
                .libraryHours(library.getLibraryHours())
                .build();
    }
}
