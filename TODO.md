# TODO 

친구 관련 기능
- [x] ids 를 이용해서 여러유저 가져오기
- [x] 친구 어떤식으로 할지 정해놓기
- [x] 실제로 jpa 관련해서 적용되는지 테스트 (entity 만들기 + repository 를 통한 테스트 -> user repository 이용?? 그러면… service 를 만들고 이를 통해서 사용해야 할 것 같구먼…ㅎ)
- [ ] 내가 진행한 사항에 대해서... 위키에 정리하기 (왜 이런 결정을 했는지, 어떤 부분이 문제였는지, 다른 사람들은 어떻게 했는지)
- [x] user 를 삭제할 경우에 관한 처리 (관련된 user가 삭제되면 어떻게 되려나?)
- [ ] 동일한 친구 추가 시도 
    - 여러 컬럼으로 unique 체크 
    - DataIntegrityViolationException 발생
- [x] 컨트롤러 구현  
    - [x] 친구추가(외부에서 어떤 식으로 요청을 보낼까?)  
        - 추가할 userId 를 사용 /friends [POST] {friend_id=10}  
            - 성공: CREATED 201  
            - 실패: FORBIDDEN 403  
        - 친구추가 버튼 (친구인 상태이면 비활성화, 친구아니면 친구기능 동작하도록)
            - 로그인 user + friendId 로... 해당 버튼을 처리해야함

        - /friendships {friendId: id}
        - 결과적으로는 .... loginedUserId, friendId 가 필요
    - [ ] 친구조회? (UserOutline)
    - [ ] 프론트와 연동



일단은 rdb로 구현을 해보자 (간단하게라도?? 그러면 왜 그래프db가 필요한지가 좀… 직접적으로 체감이 될듯)
그래프 db 
http://tech.kakao.com/2016/01/29/opensource-1-s2graph/


`
show tables;

select * from user;
insert into user values (null, '2019-09-20', '2019-09-20', '20190727', 'coverUrl', 'hello1@hello.com', 'asdfsdafsafaasdf', 'man', 'hello world', 'eunsukko');
insert into user values (null, '2019-09-20', '2019-09-20', '20190727', 'coverUrl',  'hello2@hello.com', 'asdfsdafsafaasdf', 'man', 'hello world', 'friend');

select * from friendship;
insert into friendship values (1, 1, 2);

select * from friendship;


## 테스트 command
$ curl -X POST -H "Content-Type: application/json; charset=utf-8" -d '{"friendId":"5"}' localhost:8080/api/friendship

// 위에서 friendId 임에 주의 (friend_id 인줄 알았는데... ㅠㅠ)

## 친구라는 것이 문제가 되는 것??

일단… 나의 친구 검색 => 전체 User 테이블 검색
…
ex. 나의 뉴스피드에… 친구들의 글을 가져오기… (글에서 해당하는 친구들의 글을.. 모두 검색…)
1. 친구수 * [User 테이블 전체 검색]
2. [User 테이블 전체 검색] + 동적인 갯수의 … 쿼리 적용 



## 프론트 display 설정 (console에서)
document.getElementById('add-friend').style.display = "none"


## 프론트 이슈?? 
- 여러파일에서 .js 파일을 사용할 경우 (getElementById 를 사용한다면.... 다른곳에서 쓰이는 js파일이 먼저 로딩될수도...... -> 네임스페이스를 정해주어야할까? -> 아 ??? 각자의 html파일에서만 보이려나??)


참고
1. in clause [https://www.w3schools.com/sql/trysql.asp?filename=trysql_select_in]
2. JPA repository [https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords] - repository 에서 어떻게 in 을 적용할지 (여러 id들로 해당하는 user 검색)
3. OnDelete [https://stackoverflow.com/questions/7197181/jpa-unidirectional-many-to-one-and-cascading-delete]