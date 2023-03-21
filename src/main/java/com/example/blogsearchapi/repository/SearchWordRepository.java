package com.example.blogsearchapi.repository;

import com.example.blogsearchapi.domain.SearchWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchWordRepository extends JpaRepository<SearchWord, Long> {

    Optional<SearchWord> findByName(String name);

    List<SearchWord> findTop10ByOrderByCountDesc();
}
