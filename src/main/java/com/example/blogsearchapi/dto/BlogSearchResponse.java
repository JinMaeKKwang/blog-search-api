package com.example.blogsearchapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class BlogSearchResponse {

    private final int itemCount;
    private final List<Items> items;
    private final PageInfo pageInfo;

    public static BlogSearchResponse of(KakaoBlogSearchRes res) {
        List<Items> items = res.getDocuments().stream()
                .map(document -> Items.of(document))
                .collect(Collectors.toList());
        PageInfo pageInfo = PageInfo.builder()
                .count(res.getMeta().getPageableCount())
                .isEnd(res.getMeta().isEnd())
                .build();
        return BlogSearchResponse.builder()
                .itemCount(res.getDocuments().size())
                .items(items)
                .pageInfo(pageInfo)
                .build();
    }

    public static BlogSearchResponse of(NaverBlogSearchRes res) {
        List<Items> items = res.getItems().stream()
                .map(item -> Items.of(item))
                .collect(Collectors.toList());
        PageInfo pageInfo = PageInfo.builder()
                .count(res.getTotal())
                .isEnd(res.getStart() == 50)
                .build();
        return BlogSearchResponse.builder()
                .itemCount(res.getItems().size())
                .items(items)
                .pageInfo(pageInfo)
                .build();
    }

    @Getter
    @Builder
    public static class Items {
        private String url;
        private String title;
        private String thumbnail;
        private String datetime;
        private String contents;
        private String blogname;

        public static Items of(KakaoBlogSearchRes.Documents document) {
            return Items.builder()
                    .url(document.getUrl())
                    .title(document.getTitle())
                    .thumbnail(document.getThumbnail())
                    .datetime(document.getDatetime())
                    .contents(document.getContents())
                    .blogname(document.getBlogname())
                    .build();
        }

        public static Items of(NaverBlogSearchRes.Items items) {
            return Items.builder()
                    .url(items.getLink())
                    .title(items.getTitle())
                    .datetime(items.getPostdate())
                    .contents(items.getDescription())
                    .blogname(items.getBloggername())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PageInfo {
        private int count;
        private boolean isEnd;
    }
}
