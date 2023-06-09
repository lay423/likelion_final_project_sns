# FinalProject_HwangJunha_team3
## 멋쟁이사자처럼 개인 프로젝트


<br>

### 첫번째 미션 필수 과제
- 회원가입
- Swagger
- AWS EC2에 Docker 배포
- Gitlab CI & Crontab CD
- 로그인
- 포스트 작성, 수정, 삭제, 리스트

<br>
#### user-controller
<br>
POST ​/api​/v1​/users​/join
회원가입

POST ​/api​/v1​/users​/login
로그인
<br>
<br>
#### post-controller
GET ​/api​/v1​/posts
포스트 리스트

POST ​/api​/v1​/posts
포스트 작성

PUT ​/api​/v1​/posts​/{id}
포스트 수정

DELETE ​/api​/v1​/posts​/{postId}
포스트 삭제

GET ​/api​/v1​/posts​/{postsId}
포스트 상세

### 요구사항 분석
- [x] 인증 / 인가 필터 구현
  - 인증 / 인가 필터를 구현하기 위해 JwtTokenFilter를 사용해야 겠다고 생각했습니다.
- [x] 상품 조회 / 수정 API 구현
  - 요구사항에 맞는 정확한 반환값을 지키고자 생각을 많이 했습니다.
- [x] 상품 수정 테스트 코드 작성
  - 요구사항에 맞는 테스트 코드를 작성하고자 했습니다.
- [x] Swagger를 이용하여 상품 수정 API 문서 자동화
  - swagger 3.0.0을 이용해서 구현해야 겠다고 생각했습니다.
- [x] develop 브랜치에 push 할 경우 AWS EC2 서버에 자동으로 배포 되도록 구현
  - crontab을 이용하여 deploy.sh가 1분마다 실행되도록 하여 구현했습니다. newly_pulled가 발생하면 기존 container를 종료하고 새로 컨테이너를 실행하도록 했습니다.

### 특이사항
#### 아쉬웠던 점
- 포스트 상세기능을 만들 때, 요구사항을 처음에 숙지하지 못하고 개발을 하여 수정하는 데에 골머리를 싸야 했습니다. 다음 번 프로젝트에는 요구사항을 잘 숙지하고 개발을 시작해야겠다는 점을 배웠습니다.


### 두번째 미션 필수과제
- 댓글
- 좋아요
- 마이피드
- 알림
- Swagger에 ApiOperation을 써서 Controller 설명 보이게 할 것

#### post-controller
GET /posts/{postId}/comments[?page=0]
댓글 조회

POST /posts/{postsId}/comments
댓글 작성

PUT /posts/{postId}/comments/{id}
댓글 수정

DELETE /posts/{postsId}/comments/{id}
댓글 삭제

POST /posts/{postId}/likes
좋아요 누르기

GET /posts/{postsId}/likes
좋아요 개수

GET /posts/my
마이피드 조회 기능

#### alarm-controller

GET /alarms
특정 사용자의 글에 대한 알림 조회
