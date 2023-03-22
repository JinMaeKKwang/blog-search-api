

# Blog Search API

카카오/네이버 OPEN API를 활용하여 블로그 검색결과를 응답하는 API.

### 빌드 결과물 
https://github.com/JinMaeKKwang/blog-search-api-build

### Swagger UI
 http://localhost:8080/swagger-ui/index.html



# API 명세서

## 블로그 검색
카카오/네이버 OPEN API와 연동하여 블로그 검색결과를 JSON 형식으로 반환한다.

### 요청 URL
http://localhost:8080/api/search

### HTTP 메서드
GET

### 파라미터
파라미터를 쿼리 스트링 형식으로 전달한다.

| NAME  | TYPE    | 필수 여부 | 설명                                                      |
|-------|---------|-------|---------------------------------------------------------|
| query | String  | Y     | 검색어. 단어의 길이는 1이상입니다.                                    |
| sort  | String  | Y     | 결과 문서의 정렬방식을 설정합니다. <br/>accuracy(정확도순) 혹은 recency(최신순) |
| page  | Integer | Y     | 결과 페이지의 번호. 1이상 50이하.                                   |
| size  | Integer | Y     | 한 페이지에 보여줄 검색 결과 개수. 1이상 50이하.                          |

### 요청 예
```agsl
curl --location 'http://localhost:8080/api/search?query=%EB%A7%9B%EC%A7%91&sort=accuracy&page=1&size=5'
```

### 응답
| NAME            | TYPE     | 설명                |
|-----------------|----------|-------------------|
| itemsCount      | Integer  | items의 개수.        |
| items.url       | String   | 블로그 글 URL.        |
| items.title     | String   | 블로그 글 제목.         |
| items.thumbnail | String   | 블로그 글 썸네일.        |
| items.datetime  | Datetime | 블로그 글 작성시간.       |
| items.contents  | String   | 블로그 글 요약          |
| items.blogname  | String   | 블로그의 이름           |
| PageInfo.count  | Integer  | 총 검색된 결과 수        |
| PageInfo.end    | Boolean  | 현재 페이지가 마지막인지 여부. |

### 응답 예
```agsl
{
  "itemCount": 1,
  "items": [
    {
      "url": "http://croboda.tistory.com/45",
      "title": "서귀포 <b>맛집</b> BEST",
      "thumbnail": "https://search4.kakaocdn.net/argon/130x130_85_c/B5EoggjDI3j",
      "datetime": "2023-03-08T13:44:19.000+09:00",
      "contents": "서귀포 <b>맛집</b> BEST 색달식당 방문기 안녕하세요 크로노입니다. 이번에 거의 몇년만에 가족들과 제주도 여행에 다녀왔습니다. 둘째날 주변 지인들에게 소개받은 서귀포 <b>맛집</b> 색달식당엘 다녀왔어요. 방송에도 출연할 만큼 갈치 요리가 일품인 곳으로 세트 요리를 주문하면 같이 차려지는 기본 반찬들까지 맛깔났던 곳...",
      "blogname": "크로노"
    }
  ],
  "pageInfo": {
    "count": 800,
    "end": false
  }
}
```

## 인기 검색어 목록
사용자들이 많이 검색한 검색 키워드를 최대 10개 제공한다.

### 요청 URL
http://localhost:8080/api/popularSearch

### HTTP 메서드
GET

### 파라미터
파라미터 없음.

### 요청 예
```agsl
curl --location 'http://localhost:8080/api/popularSearch'
```

### 응답
| NAME        | TYPE    | 설명         |
|-------------|---------|------------|
| itemsCount  | Integer | items의 개수. |
| items.name  | String  | 검색어.       |
| items.count | Integer | 검색 횟수.     |

### 응답 예
```agsl
{
  "itemCount": 2,
  "items": [
    {
      "name": "맛집",
      "count": 4
    },
    {
      "name": "여행",
      "count": 1
    }
  ]
}
```