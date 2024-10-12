package com.hongik.service.library;

import com.hongik.domain.library.Library;
import com.hongik.domain.library.LibraryRepository;
import com.hongik.dto.library.response.LibraryResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public LibraryResponse getLibraryHours() {
        Library library = libraryRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new AppException(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));

        return LibraryResponse.of(library);
    }

    @Transactional
    public LibraryResponse updateLibraryHours(final int libraryHours) {
        Library library = libraryRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new AppException(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));

        library.updateLibraryHours(libraryHours);

        return LibraryResponse.of(library);
    }
}
