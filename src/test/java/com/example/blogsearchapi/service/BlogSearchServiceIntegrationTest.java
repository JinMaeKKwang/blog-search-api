package com.example.blogsearchapi.service;

import com.example.blogsearchapi.domain.SearchWord;
import com.example.blogsearchapi.dto.BlogSearchRequest;
import com.example.blogsearchapi.dto.KakaoBlogSearchRes;
import com.example.blogsearchapi.dto.NaverBlogSearchRes;
import com.example.blogsearchapi.repository.SearchWordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BlogSearchServiceIntegrationTest {

    @Autowired
    private BlogSearchService blogSearchService;

    @Autowired
    private SearchWordRepository searchWordRepository;

    @Test
    public void search_100개의_쓰레드로_동시성처리_테스트() throws InterruptedException {
        //given
        int threadCount = 100, errorCount = 0;
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest("맛집", "accuracy", 1, 5);
        ExecutorService executorService = Executors.newFixedThreadPool(32); //인자 개수만큼 고정된 쓰레드풀을 만듬.
        CountDownLatch latch = new CountDownLatch(threadCount); //다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와줌.
        List<Future> futures = new ArrayList<>();
        //when
        for (int i = 0; i < threadCount; i++) {
            //멀티쓰레드로 처리할 작업을 예약.
            futures.add(executorService.submit(() -> {
                try {
                    blogSearchService.search(blogSearchRequest);
                } finally {
                    latch.countDown(); //CountDownLatch의 count--
                }
            }));
        }
        latch.await(); //모든 쓰레드가 작업이 완료되어서 CountDownLatch의 count=0일 경우 해제됨.
        for (Future future: futures) {
            try {
                future.get();
            } catch (Exception e) {
                System.out.println("충돌");
                System.out.println(e.getMessage());
                errorCount++;
            }
        }
        //then
        Optional<SearchWord> optional = searchWordRepository.findByName("맛집");
        if (optional.isPresent()) {
            SearchWord searchWord = optional.get();
            assertThat(searchWord.getCount()).isEqualTo(threadCount - errorCount);
        }
    }

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