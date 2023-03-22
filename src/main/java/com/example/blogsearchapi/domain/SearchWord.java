package com.example.blogsearchapi.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchWord {
    @Id @GeneratedValue
    @Column(name = "search_id")
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer count;

    private String createdName;

    private LocalDateTime createdAt;

    private String lastModifiedName;

    private LocalDateTime lastModifiedAt;

    @Version
    private Integer version;

    @Builder
    public SearchWord(String name, String createdName, LocalDateTime createdAt, String lastModifiedName, LocalDateTime lastModifiedAt, Integer version) {
        this.name = name;
        this.count = 1;
        this.createdName = createdName;
        this.createdAt = createdAt;
        this.lastModifiedName = lastModifiedName;
        this.lastModifiedAt = lastModifiedAt;
        this.version = version;
    }

    public void updateCount() {
        this.count = this.count + 1;
        this.lastModifiedName = "Search-API";
        this.lastModifiedAt = LocalDateTime.now();
    }
}
