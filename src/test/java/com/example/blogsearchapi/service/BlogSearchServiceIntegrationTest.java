package com.example.blogsearchapi.service;

import com.example.blogsearchapi.dto.BlogSearchRequest;
import com.example.blogsearchapi.dto.KakaoBlogSearchRes;
import com.example.blogsearchapi.dto.NaverBlogSearchRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class BlogSearchServiceIntegrationTest {

    @Autowired
    private BlogSearchService blogSearchService;

    @Test
    public void callKakaoSearchApi_성공() {
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("맛집", "accuracy", 1, 5);
        //when
        ResponseEntity<KakaoBlogSearchRes> responseEntity = blogSearchService.callKakaoSearchApi(blogSearchRequest);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getDocuments().size()).isEqualTo(5);
    }

    @Test
    public void callKakaoSearchApi_실패() {
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("", "accuracy", 1, 5);
        //when
        //then
        assertThatThrownBy(() -> blogSearchService.callKakaoSearchApi(blogSearchRequest))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("400 Bad Request");
    }

    @Test
    public void callNaverSearchApi_성공() {
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("맛집", "accuracy", 1, 5);
        //when
        ResponseEntity<NaverBlogSearchRes> responseEntity = blogSearchService.callNaverSearchApi(blogSearchRequest);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getItems().size()).isEqualTo(5);
    }

    @Test
    public void callNaverSearchApi_실패() {
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("", "accuracy", 1, 5);
        //when
        //then
        assertThatThrownBy(() -> blogSearchService.callNaverSearchApi(blogSearchRequest))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("400 Bad Request");
    }
}