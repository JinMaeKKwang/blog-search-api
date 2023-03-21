package com.example.blogsearchapi.controller;

import com.example.blogsearchapi.dto.BlogSearchRequest;
import com.example.blogsearchapi.dto.BlogSearchResponse;
import com.example.blogsearchapi.dto.PopularSearchResponse;
import com.example.blogsearchapi.service.BlogSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Search", description = "블로그 검색 관련 API")
@RestController
@Slf4j
@RequiredArgsConstructor
public class BlogSearchController {

    private final BlogSearchService blogSearchService;

//    @GetMapping("/api/search")
//    public String search(
//            @RequestParam(name = "query", required = false) String query,
//            @RequestParam(name = "sort", required = false, defaultValue = "accuracy") String sort,
//            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
//            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
//        log.info(query);
//        log.info(sort);
//        log.info(page + "");
//        log.info(size + "");
//        blogSearchService.search(query, sort, page, size);
//        return "search";
//    }

    @Operation(summary = "블로그 검색", description = "카카오/네이버 OPEN API와 연동하여 블로그를 검색하는 API 입니다.")
    @GetMapping("/api/search")
    public BlogSearchResponse search(@ParameterObject @Valid BlogSearchRequest blogSearchRequest) {
        log.info("/api/search 요청 : {}", blogSearchRequest);
        return blogSearchService.search(blogSearchRequest);
    }

    @Operation(summary = "TOP10 인기 검색어 목록", description = "사용자들이 많이 검색한 검색 키워드를 최대 10개 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PopularSearchResponse.class)))
    })
    @GetMapping("/api/popularSearch")
    public PopularSearchResponse popularSearch() {
        log.info("/api/popularSearch 요청");
        return blogSearchService.popularSearchList();
    }
}
