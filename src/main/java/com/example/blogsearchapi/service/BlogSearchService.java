package com.example.blogsearchapi.service;

import com.example.blogsearchapi.domain.SearchWord;
import com.example.blogsearchapi.dto.*;
import com.example.blogsearchapi.error.ErrorCode;
import com.example.blogsearchapi.error.exception.BusinessException;
import com.example.blogsearchapi.repository.SearchWordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BlogSearchService {
    private static final String KAKAO_HOST = "https://dapi.kakao.com";
    private static final String KAKAO_SEARCH_BLOG_URL = "/v2/search/blog";
    private static final String KAKAO_RESTAPI_KEY = "055dd3cda7408b1c1d051212aa1a0cd2";

    private static final String NAVER_HOST = "https://openapi.naver.com";
    private static final String NAVER_SEARCH_BLOG_URL = "/v1/search/blog.json";
    private static final String NAVER_CLIENT_ID = "lVAWg6gh8H7zJi1LGGsI";
    private static final String NAVER_CLIENT_SECRET = "DY5nhz8aGc";

    @Autowired 
    RestTemplate restTemplate;
    
    @Autowired
    SearchWordRepository searchWordRepository;

    public BlogSearchResponse search(BlogSearchRequest blogSearchReq) {
        Optional<SearchWord> optionalSearchWord = searchWordRepository.findByName(blogSearchReq.getQuery());
        if (optionalSearchWord.isPresent()) {
            SearchWord searchWord = optionalSearchWord.get();
            searchWord.updateCount();
            searchWordRepository.save(searchWord);
        } else {
            SearchWord searchWord = SearchWord.builder()
                    .name(blogSearchReq.getQuery())
                    .createdName("Search-API")
                    .createdAt(LocalDateTime.now())
                    .lastModifiedName("Search-API")
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            searchWordRepository.save(searchWord);
        }

        BlogSearchResponse blogSearchResponse = null;
        boolean flag = false;
        try {
            ResponseEntity<KakaoBlogSearchRes> kakaoResponseEntity = callKakaoSearchApi(blogSearchReq);
            log.info(kakaoResponseEntity.getStatusCode().toString());
            log.info(kakaoResponseEntity.getBody().toString());
            blogSearchResponse = BlogSearchResponse.of(kakaoResponseEntity.getBody());
        } catch (HttpClientErrorException e) {
            log.error(e.getStatusCode().toString());
            log.error(e.getResponseBodyAsString());
            throw new BusinessException(ErrorCode.KAKAO_CLIENT_ERR);
        } catch (HttpServerErrorException e) {
            log.error(e.getStatusCode().toString());
            log.error(e.getResponseBodyAsString());
            flag = true;
        }

        if (flag) {
            try {
                ResponseEntity<NaverBlogSearchRes> naverResponseEntity = callNaverSearchApi(blogSearchReq);
                log.info(naverResponseEntity.getStatusCode().toString());
                log.info(naverResponseEntity.getBody().toString());
                blogSearchResponse = BlogSearchResponse.of(naverResponseEntity.getBody());
            } catch (HttpClientErrorException e) {
                log.error(e.getStatusCode().toString());
                log.error(e.getResponseBodyAsString());
                throw new BusinessException(ErrorCode.NAVER_CLIENT_ERR);
            } catch (HttpServerErrorException e) {
                log.error(e.getStatusCode().toString());
                log.error(e.getResponseBodyAsString());
                throw new BusinessException(ErrorCode.NAVER_SERVER_ERR);
            }
        }

        return blogSearchResponse;
    }

    public ResponseEntity<KakaoBlogSearchRes> callKakaoSearchApi(BlogSearchRequest blogSearchReq) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(KAKAO_HOST + KAKAO_SEARCH_BLOG_URL)
                .queryParam("query", blogSearchReq.getQuery())
                .queryParam("sort", blogSearchReq.getSort())
                .queryParam("page", blogSearchReq.getPage())
                .queryParam("size", blogSearchReq.getSize())
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + KAKAO_RESTAPI_KEY);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, httpEntity, KakaoBlogSearchRes.class);
    }

    public ResponseEntity<NaverBlogSearchRes> callNaverSearchApi(BlogSearchRequest blogSearchReq) {
        String sort = blogSearchReq.getSort();
        if ("accuracy".equals(sort)) {
            sort = "sim"; //정확도순으로 내림차순 정렬
        } else if ("recency".equals(sort)) {
            sort = "date"; //날짜순으로 내림차순 정렬
        }
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(NAVER_HOST + NAVER_SEARCH_BLOG_URL)
                .queryParam("query", blogSearchReq.getQuery())
                .queryParam("sort", sort)
                .queryParam("start", blogSearchReq.getPage())
                .queryParam("display", blogSearchReq.getSize())
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Naver-Client-Id", NAVER_CLIENT_ID);
        httpHeaders.set("X-Naver-Client-Secret", NAVER_CLIENT_SECRET);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, httpEntity, NaverBlogSearchRes.class);
    }

    public PopularSearchResponse popularSearchList() {
        List<SearchWord> searchWordList = searchWordRepository.findTop10ByOrderByCountDesc();
        return PopularSearchResponse.of(searchWordList);
    }
}
