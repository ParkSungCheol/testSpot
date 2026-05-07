# testSpot

Spring Boot 초기 설정 프로젝트

## 프로젝트 구조

```
testspot/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/testspot/
│   │   │       ├── TestSpotApplication.java
│   │   │       └── controller/
│   │   │           └── TestController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── .gitignore
```

## 빌드 및 실행

### 사전 요구사항
- Java 17 이상
- Maven 3.6 이상

### 실행 방법

```bash
# 프로젝트 클론
git clone https://github.com/ParkSungCheol/testSpot.git
cd testSpot

# 빌드
mvn clean install

# 실행
mvn spring-boot:run
```

## 테스트

애플리케이션이 시작되면 다음 URL에 접속할 수 있습니다:

```bash
# 브라우저에서
http://localhost:8080/test

# 또는 curl 명령어로
curl http://localhost:8080/test
```

**응답**: `hello`

## 설정

- **포트**: 8080
- **애플리케이션 이름**: testspot

`src/main/resources/application.properties` 파일에서 설정을 변경할 수 있습니다.
