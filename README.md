# webflux-boilerplate

webflux 형태로 보일러플레이트 구현 

## 구조

WebFlux + R2DBC/~~R2DBC-QUERYDSL~~ + Mysql(Replication master/slave)

## 진행중 마주친 내용

너무 `DataSource` 에 익숙해진 나머지 `ConnectionFactory` 라는게 익숙하지않았다.
또한 `Mybatis` 나 `JPA` 할때는 `Properties` 상속받아서 옵션을 마음대로 설정할수있었는데  
아직 `R2DBC` 나 다른 리액티브모듈에는 찾아보기 힘들다.

`R2DBC` 자체에는 관계형 아노테이션들을 지원하지않는다고한다.  
단순하게 프로젝트를 가져가기위한것도 있다고하는데 나중에 추가될지 안될지는 모르겠다.  
즉 `JPA` 처럼 `ManyToOne` 같은것을 할수가없고 그냥 `RDB` 쿼리하듯이 해야함.

그래서 일반적인 `JPA` 를 쓰기에는.. 웹 로직은 논블록킹으로 하지만  
디비의 데이터를 가져올때는 블록킹인게 좀 걸렸다.(이도저도아닌듯한..)

일단 이거대로 어떻게 써먹을수있을까? 생각하다가 `r2dbc` 쿼리를 짜보았는데  
코드는 `SchoolService` 쪽에 존재한다.  
여기까지는 마음에 들긴하지만 테이블명이나 컬럼등은 하드코딩으로 넣는게 이쁘지않았다.
```java
    public Mono<School> findByIdAndName(Long id, String name) {
        return mainEntityTemplate.select(School.class)
                .from("school")
                .matching(Query.query(
                        where("id").is(Objects.requireNonNull(id))
                                .and(where("name").is(Objects.requireNonNull(name)))
                )).one();
    }
```

`reactive`를 지원하는 `querydsl`이 없을까? 찾아보니 있었다..  
출처쪽에서 확인이 가능한데 아직 막 나오고있는거다보니 뭔가 잘안된다 ㅠ  
일단 `flyway` 유무에 따라 `bean` 만들어야할게 갈라지는데 문서대로 했지만  
뭔가 필요한 메소드가 존재하지않는다라는 말만 뜨고있다..흑흑  
그래서 의존은 주석처리 

## 결론 

무조건 논블록킹으로 해야한다면 그냥 `node` 로 만들어 띄우는게 낫다는게 현재의 생각.  
아직 사용하기에는 초창기이구 그런 이슈들도 모두 처리해낼수있다! 라면 반대는 안하지만..  
개인적인 생각으로는 현재로써는 `Spring MVC` 로도 충분하다고 생각함

***

출처

1. [r2dbc querydsl github](https://github.com/infobip/infobip-spring-data-querydsl)
2. [r2dbc mysql github](https://github.com/mirromutth/r2dbc-mysql)