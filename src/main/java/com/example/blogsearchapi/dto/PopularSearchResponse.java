package com.example.blogsearchapi.dto;

import com.example.blogsearchapi.domain.SearchWord;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PopularSearchResponse {

    private int itemCount;
    private List<Items> items;

    public static PopularSearchResponse of(List<SearchWord> list) {
        List<Items> items = list.stream()
                .map(searchWord -> Items.of(searchWord))
                .collect(Collectors.toList());
        return PopularSearchResponse.builder()
                .itemCount(items.size())
                .items(items)
                .build();
    }

    @Getter
    @Builder
    public static class Items {
        private String name;
        private int count;

        public static Items of(SearchWord searchWord) {
            return Items.builder()
                    .name(searchWord.getName())
                    .count(searchWord.getCount())
                    .build();
        }
    }
}
