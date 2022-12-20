#### 도커 이미지 전체 삭제 명령어
```
docker rmi $(docker images -q)
```

#### 도커 컨테이너 전체 삭제 명령어
```
docker rm -f $(docker ps -aq)
```
#### 도커 컨테이너 네트워크 탐색
```
docker network inspect bridge
```
#### 도커 특정 컨테이너 네트워크 상태 상세보기 
```
docker network ls
```
#### 도커 컨테이너 실시간 로그 보기 명령어
```
docker logs -f [NAME OR CONTAINER ID]
```
#### docker inspect 는 container or image 의 low level 정보를 가져오는 명령어
```
docker inspect [CONTAINER ID]
```

#### 1. Dockerfile
1) 목적

    - base image 파일로 수정된 image 만드는 일련의 과정들을 정리해 놓은 파일이다. 

    - docker는 Dockerfile을 이용하여 손쉽게 동일한 이미지를 반복해서 만들수 있다.

#### 2. docker-compose.yml
1) 목적

    - 연결된 다수의 container를 하나로 통합하여 관리 하는 도구

2) 유의 사항

    - docker-compose는 host level에서만 동작한다.

    - docker swarm에 배포 하기 위해서는 docker stack deploy를 사용해야한다..

3) docker-compose.yml

     - container실행 옵션을 미리 정의한 문서 입니다. 

     - docker-compose를 docker-compose.yml에 정의된 데로 container를 실행 합니다.