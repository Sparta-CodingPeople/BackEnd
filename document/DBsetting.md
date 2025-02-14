# macbook 설치 방법

1. 터미널에서 명령어 실행
```shell
brew install postgresql@16
```

2. 환경변수 설정
```shell
echo 'export PATH="/opt/homebrew/opt/postgresql@16/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
export LDFLAGS="-L/opt/homebrew/opt/postgresql@16/lib"
export CPPFLAGS="-I/opt/homebrew/opt/postgresql@16/include"
```

3. 서비스 시작
```shell
brew services start postgresql@16
```

4. 버전 확인
```shell
postgres --version
```

5. postgresql 접속
```shell
psql postgres
```

6. 사용자 생성
```shell
postgres=# CREATE ROLE testuser WITH LOGIN PASSWORD '비밀번호설정';
```

7. 데이터베이스 생성
```shell
CREATE DATABASE delivery WITH OWNER admin;
```

8. 데이터베이스 출력
```shell
\l+
```
