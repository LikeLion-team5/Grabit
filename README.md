# Grabit
멋쟁이 사자처럼 백엔드스쿨 3기 : 음식점 예약 시스템

![image](https://github.com/LikeLion-team5/Grabit/assets/92236489/c18a0058-1667-4e0e-af6c-991c52737a6d)

프로젝트 기간 : 2023.05.22 - 2023.06.16
<br>

## 팀원 소개
|  [김희연](https://github.com/heeyeon3050)  | [김민우](https://github.com/Kminwoo97) | [문성민](https://github.com/seongmin8636) | [최혁](https://github.com/Choi-Hyeok99) |
|:---------------------------------------:|:-----------------------------------:|:--------------------------------------:|:-------------------------------------:|
|                   팀장                    |                 팀원                  |                   팀원                   |                  팀원                   |
|          - 회원 프론트<br>- 리뷰 도메인           |      - 식당 백엔드<br> - 토스페이먼츠 결제       |        - 식당 프론트엔드<br> - 예약 도메인         |        - 회원 백엔드<br> - CI/CD 배포        |



<br>

## 시연영상

<a href="https://www.youtube.com/watch?v=sHvjV5fP4bU">grabit 시연영상</a>

## 배포 링크

<a href="https://grabit.run/">그래빗 배포 링크</a>

## 기술스택

### Tech
<img src="https://img.shields.io/badge/Java-FC4C02?style=flat-square&logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring boot-6DB33F?style=flat-square&logo=Spring boot&logoColor=white"/>
        
<img src="https://img.shields.io/badge/gradle-02303A?logo=gradle&logoWidth=25"/> <img src="https://img.shields.io/badge/Spring security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-2596BE?style=flat-square&logo=&logoColor=white"/> <img src="https://img.shields.io/badge/MariaDB-2596BE?style=flat-square&logo=MariaDB&logoColor=white"/>
        
       

### Deploy
<img src="https://img.shields.io/badge/Nave Cloud Platform-03C75A?style=flat-square&logo=naver&logoColor=white"/> <img src="https://img.shields.io/badge/Github Actions-2AB1AC?style=flat-square&logo=github&logoColor=black"/> <img src="https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-%230db7ed.svg?style=flat-square&logo=docker&logoColor=white"/> 
        

### Tool
<img src="https://img.shields.io/badge/IntelliJ IDEA-0052CC?style=flat-square&logo=IntelliJ IDEA&logoColor=black"/> <img src="https://img.shields.io/badge/Github-000000?style=flat-square&logo=Github&logoColor=white"/> <img src="https://img.shields.io/badge/Notion-FFFFFF?style=flat-square&logo=Notion&logoColor=black"/> 


<br>


## 아키텍쳐
<img width="874" alt="image" src="https://github.com/LikeLion-team5/Grabit/assets/92236489/ffc48237-dfb6-4924-8e99-aa5d12be0947">

<br><br>


## ERD
<img width="1104" alt="image" src="https://github.com/LikeLion-team5/Grabit/assets/92236489/fa3b3fcf-356f-4ac9-9cd2-78182c917d86">

<br><br>

## 요구사항 분석
<img width="1373" alt="image" src="https://github.com/LikeLion-team5/Grabit/assets/92236489/684b8d2f-c327-4ff2-a86d-63900a4ab265">


## 프로젝트 실행 방법

1. 프로젝트 다운받기

2. src/main/resources 에 application-secret.yml 작성한다.
```yml
spring:
  datasource:
    url: jdbc:mariadb://DB주소:포트번호/DB명?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul
    driver-class-name: org.mariadb.jdbc.Driver
    username: 계정이름
    password: 비밀번호

  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        use_sql_comments: true

  security:
    oauth2:
      client:
        registration:
          naver:
            clientId: '네이버에서 발급받은 id'
            client-secret: '네이버에서 발급받은 시크릿 키'
          kakao:
            clientId: '카카오에서 발급받은 키'



cloud:
  ncp:
    credentials:
      access-key: 'NCP 발급받은 access key'
      secret-key: 'NCP 발급받은 secret key'
    stack:
      auto: false
    region:
      static: ap-northeast-2
    s3:
      endpoint: https://kr.object.ncloudstorage.com
      bucket: '버킷명'
      dir: '버킷안에서 사용할 디렉토리명'
```
