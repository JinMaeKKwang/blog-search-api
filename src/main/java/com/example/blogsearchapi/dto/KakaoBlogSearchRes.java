package com.example.blogsearchapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KakaoBlogSearchRes {

    private List<Documents> documents;
    private Meta meta;

    @Data
    public static class Documents {
        private String url;
        private String title;
        private String thumbnail;
        private String datetime;
        private String contents;
        private String blogname;
    }

    @Data
    public static class Meta {
        @JsonProperty("total_count")
        private int totalCount;
        @JsonProperty("pageable_count")
        private int pageableCount;
        @JsonProperty("is_end")
        private boolean isEnd;
    }

}
