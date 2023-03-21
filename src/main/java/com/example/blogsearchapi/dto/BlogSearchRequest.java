package com.example.blogsearchapi.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter @Setter
@ToString
@AllArgsConstructor
public class BlogSearchRequest {

    @Parameter(description = "검색어를 입력하세요. 단어의 길이는 1이상입니다.", example = "맛집", required = true)
    @NotNull(message = "검색어를 입력하시오.")
    @Size(min = 1, message = "1개 이상의 단어를 입력하세요.")
    private String query;

    @Parameter(description = "결과 문서의 정렬방식을 설정합니다. accuracy(정확도순) 혹은 recency(최신순)을 입력하세요.", example = "accuracy", required = true)
    @Pattern(regexp = "accuracy|recency", message = "accuracy 또는 recency를 입력하세요.")
    private String sort;

    @Parameter(description = "결과 페이지의 번호를 입력하세요. 1~50사이의 값.", example = "1", required = true)
    @Min(value = 1, message = "1이상의 숫자를 입력하세요.")
    @Max(value = 50, message = "50이하의 숫자를 입력하세요.")
    private int page;

    @Parameter(description = "한 페이지에 보여줄 문서의 수를 입력하세요. 1~50사이의 값.", example = "5", required = true)
    @Min(value = 1, message = "1이상의 숫자를 입력하세요.")
    @Max(value = 50, message = "50이하의 숫자를 입력하세요.")
    private int size;
}
