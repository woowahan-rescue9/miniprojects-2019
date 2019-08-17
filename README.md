# miniprojects-2019

## Controller 
- [ ] ResponseEntity static factory method
- [ ] UserApiController api uri 추가

## Domain
- [x] DateTime Entity rename
- [ ] Article
    - [ ] isPresent rename
    - [ ] equals, hashcode, toString
- [ ] User
    - [ ] gender enum 처리
    - [ ] validation
- [ ] Comment
    - [ ] equals, hashcode, toString
- [ ] ArticleLike
    - [ ] equals, hashcode, toString
- [ ] CommentLike
    - [ ] equals, hashcode, toString


## Service
- [ ] ArticleService
    - [ ] getArticle() : JPA @Where 어노테이션 적용 or JPA쿼리에서 값만가져오기
    - [ ] like() : delete가 정상적으로 되는지 확인 필요(영속성 등록이 안된 객체임)
- [ ] LoginService
    - [ ] checkEncryptedPassword 확인
- [ ] UserDto
    - [ ] validation
- [ ] CommentLikeResponse
    - [ ] isLiked 대신에 HttpStatus로 응답
- [x] Assembler 통일성 맞추기(빈으로 생성하던지, static method로 생성하던지)


## Test
- [ ] Article, Comment Controller
    - [ ] Like 테스트 추가
    - [ ] CRUD 공통 로직 추상화
- [ ] Article, Comment Service
    - [ ] like 테스트 다시 수정
    

## pakage
- [ ] service, controller 도에민 별 패키지 분리


    