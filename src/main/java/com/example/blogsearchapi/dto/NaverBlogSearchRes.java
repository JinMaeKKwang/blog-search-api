package com.example.blogsearchapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class NaverBlogSearchRes {

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Items> items;

    @Data
    public static class Items {
        private String title;
        private String link;
        private String description;
        private String bloggername;
        private String bloggerlink;
        private String postdate;
    }
}
