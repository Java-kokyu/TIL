### GitHub Actions + Spring Boot + AWS EC2 + Redis 사용시 발생했던 오류들

Docker에 대해서 깊이 있게 공부하지 않은 상태에서, 당장 개발 서버에 적용해야 하다보니 많은 시간을 잡아먹게 되었다. 내가 진행하고 있는 프로젝트에서는 본인 확인을 위해 메일/메세지로 보내는 인증번호를 저장하기 위해 Redis를 사용하게 되었고, 자동 배포를 진행하기 위해서 Docker와 GitHub Actions을 사용했다.
문제는 아무리 레포를 찾아봐도 Redis와 .jar로 구성되어있는 Applciation을 관리해주는 docker-compose.yaml을 활용하는 법을 이해할 수 없었고(일단 프로젝트에서 제대로 적용이 되지 않았다.) 몇 시간을 허비하다가 해결이 되어 방법을 공유해보도록 한다.
1. Spring Boot Application과 Redis는 Docker를 사용하여 이미지를 통하게 된다면 사용하는 IP주소가 다르다. 따라서 EC2에 접속하여 network를 연결해주기 위해 브릿지를 생성하였다.
    ```
    docker network create --driver bridge redis-bridge
    ```
2. Redis 서버는 일단 1개만 사용할 것이므로, 1에서 생성한 redis 서버를 redis-bridge에 할당한 네트워크 대역에 위치시키고 작동시킨다.
   ```
   docker run -d --name redis --net redis-bridge redis
   ```
3. GitHub Actions 설정을 위한 workflow .yaml 
   ```yaml
       - name: 배포
         uses: appleboy/ssh-action@master
         with:
           host: ${{ secrets.HOST }}
           username: ubuntu
           key: ${{ secrets.PRIVATE_KEY }}
           envs: GITHUB_SHA
           script: |
             docker pull ${{ secrets.DOCKER_USERNAME }}/[CONTAINER ID]:${GITHUB_SHA::7}
             docker tag ${{ secrets.DOCKER_USERNAME }}/[CONTAINER ID]:${GITHUB_SHA::7} [CONTAINER ID]
             docker stop [CONTAINER ID]
             sleep 5
             docker run -d --rm --net redis-bridge --name [CONTAINER ID] -p 8080:8080 kurrant_v1
   ```
   redis-bridge 네트워크를 사용하여 이미지를 run 시켰다.

#### 1\. docker 설치 후 /var/run/docker.sock의 permission denied 발생

```
sudo chmod 666 /var/run/docker.sock
```

#### 2. docker build시 ssh-handshake failed 오류 발생
```
ssh-keygen -t rsa -b 4096 -C [이메일 주소] -f [컨테이너 이름]
```
<br>
<code>sudo vim ~/.ssh/authorized_keys</code> -> .pub 파일에 생성된 ssh-key 복사
<br>
<code>sudo /etc/ssh/sshd_config</code>

```
PubkeyAuthentication yes
PubkeyAcceptedKeyTypes=+ssh-rsa
```
<br>
<code>sudo service sshd restart</code>

#### 3. ssh: handshake failed: ssh: overflow reading version string
배포 과정중 ec2 HOST 명을 제대로 입력하지 않아서 발생한 오류
