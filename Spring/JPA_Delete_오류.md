### **JPA Delete가 안되는 오류**

  소셜 로그인 구현 중, SNS 계정 연동 해제를 위해서 JPA delete 메소드를 사용을 할 일이 있었는데, `repository.delete(Entity entity)`가 사용되지 않았다. `@PostMapping`에서 `@DeleteMapping`으로도 변경해보고, `try-catch`문으로 예외도 잡아보려 하고, 디버그를 찍어 어떠한 문제가 있는지 파악하려고 해도 알 수 없었다.

### **해결 전 소스 코드**
```java
@Entity
public class User {
    ...
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-fk")
    List<ProviderEmail> providerEmails;
    
    ...
}
```
```java
@Entity
public class ProviderEmail {
    ...
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-fk")
    private User user;
    
    ...
}
```

```java
@Override
@Transactional
public void disconnectSnsAccount(HttpServletRequest httpServletRequest, String sns) {
    // 유저와 연결된 소셜 로그인 정보 가져오기
    User user = commonService.getUser(httpServletRequest);
    List<ProviderEmail> providerEmails = user.getProviderEmails();

        ...
    // 특정 조건을 만족하는 providerEmail 가져오기
    ProviderEmail providerEmail = providerEmails.stream()
    .filter(e -> e.getProvider().equals(provider))
    .findAny()
    .orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        
    // 소셜로그인 연결 계정 삭제
    providerEmailRepository.deleteById(providerEmail.getId());
}
```
`disconnectSnsAccount` 메소드에서 `providerEmailRepository.deleteById(id)`와 `providerEmailRepository.delete(providerEmail)` 어떤 것을 해봐도 데이터베이스를 확인해본 결과 어떤 것도 삭제되지 않았다. 이는 `@Transcational`을 걸어 `User`를 조회한 이후에, `User`에서 가져온 `List<ProviderEmail> providerEmails` 에서 `providerEmail` 삭제해주지 않았기에 문제가 발생한 것이다. 

### **수정코드**
```java
@Override
@Transactional
public void disconnectSnsAccount(HttpServletRequest httpServletRequest, String sns) {
    // 유저와 연결된 소셜 로그인 정보 가져오기
    User user = commonService.getUser(httpServletRequest);
    List<ProviderEmail> providerEmails = user.getProviderEmails();

        ...
    // 특정 조건을 만족하는 providerEmail 가져오기
    ProviderEmail providerEmail = providerEmails.stream()
    .filter(e -> e.getProvider().equals(provider))
    .findAny()
    .orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
        
    // 소셜로그인 연결 계정 삭제
    user.getProviderEmails().removeIf(e -> e.getId().equals(providerEmail.getId()));
    providerEmailRepository.deleteById(providerEmail.getId());
}
```

### **문제 원인**
`@Transactional`을 사용하면, 메서드를 호출할 때 영속성 컨텍스트가 생긴다. 영속성 컨텍스트는 트랜잭션 AOP가 트랜잭션을 시작할 때 생겨나고, 메서드가 종료되어 트랜잭션 AOP가 트랜잭션을 커밋할 경우 영속성 컨텍스트가 flush되면서 해당 내용이 반영된다. 이후 영속성 컨텍스트 역시 종료된다. <br>
 스프링 컨테이너는 트랜잭션 범위의 영속성 컨텍스트 전략을 기본으로 사용한다. 이 전략은 이름 그대로 트랜잭션의 범위와 영속성 컨텍스트의 생존 범위가 같다는 뜻이다. 즉, **같은 트랜잭션은 같은 영속성 컨텍스트를 사용한다.**<br>
위 코드에서 삭제하고자 하는 연결 소셜로그인 계정(`providerEmail`)을 `providerEmailRepository`에서 삭제한다고 해도, 트랜잭션이 끝난 후 `user`가 가지고 있는 `List<ProviderEmail> providerEmails`에서 비교해보니 존재하지 않는 `providerEmail`이 존재하기 때문에 `user` 내부에 존재하기 때문에 `delete`가 수행되지 않았다고 생각이 된다.<br>
따라서, 위와 같이
```java
user.getProviderEmails().removeIf(e -> e.getId().equals(providerEmail.getId()));
```
와 같은 코드로 수정 할 수도 있지만
```java
@Entity
public class User {
    ...
    
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonManagedReference(value = "user-fk")
    List<ProviderEmail> providerEmails;
    
    ...
}
```
와 같이 `orphanRemoval = true` 속성으로 부모 엔티티에서 자식 엔티티의 참조를 제거하여 자식 엔티티가 자동으로 삭제 될 수 있는 코드도 만들 수 있을 것이다.