package com.example.blogsearchapi.controller;

import com.example.blogsearchapi.dto.BlogSearchRequest;
import com.example.blogsearchapi.dto.BlogSearchResponse;
import com.example.blogsearchapi.dto.PopularSearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogSearchControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void SEARCH_API_성공() {
        String url = "http://localhost:" + this.port + "/api/search";
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("맛집", "accuracy", 1, 5);
        UriComponents uriComponents = buildUriComponents(blogSearchRequest, url);
        //when
        ResponseEntity<BlogSearchResponse> responseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BlogSearchResponse.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getItems().size()).isEqualTo(5);
    }

    @Test
    public void SEARCH_API_파라미터_오류_query() {
        String url = "http://localhost:" + this.port + "/api/search";
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("", "accracy", 1, 5);
        UriComponents uriComponents = buildUriComponents(blogSearchRequest, url);
        //when
        ResponseEntity<BlogSearchResponse> responseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BlogSearchResponse.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void SEARCH_API_파라미터_오류_sort() {
        String url = "http://localhost:" + this.port + "/api/search";
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("맛집", "", 1, 5);
        UriComponents uriComponents = buildUriComponents(blogSearchRequest, url);
        //when
        ResponseEntity<BlogSearchResponse> responseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BlogSearchResponse.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void SEARCH_API_파라미터_오류_page() {
        String url = "http://localhost:" + this.port + "/api/search";
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("맛집", "accuracy", 0, 5);
        UriComponents uriComponents = buildUriComponents(blogSearchRequest, url);
        //when
        ResponseEntity<BlogSearchResponse> responseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BlogSearchResponse.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void SEARCH_API_파라미터_오류_size() {
        String url = "http://localhost:" + this.port + "/api/search";
        //given
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("맛집", "accuracy", 1, 0);
        UriComponents uriComponents = buildUriComponents(blogSearchRequest, url);
        //when
        ResponseEntity<BlogSearchResponse> responseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BlogSearchResponse.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();

    }

    @Test
    public void POPULAR_SEARCH_API_성공() {
        //given
        String searchUrl = "http://localhost:" + this.port + "/api/search";
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("맛집", "accuracy", 1, 5);
        UriComponents uriComponents = buildUriComponents(blogSearchRequest, searchUrl);
        for (int i = 0 ; i < 5; i++)
            restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BlogSearchResponse.class);
        blogSearchRequest = new BlogSearchRequest("여행", "accuracy", 1, 5);
        uriComponents = buildUriComponents(blogSearchRequest, searchUrl);
        for (int i = 0; i < 3; i++)
            restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BlogSearchResponse.class);
        blogSearchRequest = new BlogSearchRequest("집", "accuracy", 1, 5);
        uriComponents = buildUriComponents(blogSearchRequest, searchUrl);
        for (int i = 0; i < 2; i++)
            restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BlogSearchResponse.class);

        String popularSearchUrl = "http://localhost:" + this.port + "/api/popularSearch";
        UriComponents uriComponents2 = UriComponentsBuilder.fromHttpUrl(popularSearchUrl).build();
        //when
        ResponseEntity<PopularSearchResponse> responseEntity = restTemplate.exchange(uriComponents2.toString(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), PopularSearchResponse.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getItems().size()).isEqualTo(3);
    }

    private UriComponents buildUriComponents(BlogSearchRequest blogSearchReq, String url) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", blogSearchReq.getQuery())
                .queryParam("sort", blogSearchReq.getSort())
                .queryParam("page", blogSearchReq.getPage())
                .queryParam("size", blogSearchReq.getSize())
                .build();
    }
}
