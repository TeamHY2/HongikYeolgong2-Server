package com.hongik.domain.library;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "`library`")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int libraryHours;

    @Column(nullable = false, unique = true)
    private String fixedValue = "libraryHours";

    @Builder
    public Library(final int libraryHours) {
        this.libraryHours = libraryHours;
    }

    public void updateLibraryHours(final int libraryHours) {
        this.libraryHours = libraryHours;
    }
}
