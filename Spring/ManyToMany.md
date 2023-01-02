### **@ManyToMany**
데이터 설계 중, 하나의 그룹에서 다수의 유저를 가지고 있고, 유저는 여러개의 그룹을 가질 수 있는 구조를 가진 데이터 테이블을 생성해야했다. M:N 관계를 가진 테이블을 생성해야하니, @ManyToMany로 그룹 테이블과 유저 테이블을 묶으려고 했는데, 어디선가 @ManyToMany로 연관관계를 맺는 것은 좋지 않다는 글을 본 것 같아 구글링을 하게 되었다.

### **@ManyToMany를 사용하면 안 되는 이유**

<a href ="https://codeung.tistory.com/254"> 출처</a>
<a href ="https://velog.io/@guswns3371/%EC%B9%9C%EA%B5%AC-%EB%AA%A9%EB%A1%9D-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0"> 출처2</a> <br>
`@ManyToMany`는 매핑 정보만 넣는 것이 가능하고, 추가 정보를 넣는 것 자체가 불가능하다. 중간 테이블(Member_Product table)이 숨겨져 있으므로 예상하지 못한 쿼리가 나간다. 예를 들어, 데이터 보호를 위해 row를 지우지 않고 status만 변경하는 soft delete 방식을 채택할 경우 `@ManyToMany`로는 구현하지 못하게 된다.

### **중간 테이블 생성하기**
```java
@Entity
public class User {
    ...
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "user")
    @JsonBackReference(value = "user_fk")
    @Comment("그룹 정보")
    private List<UserGroup> userGroupList;
    ...
}
```

```java
@Entity
public class Group {
    ...
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "user")
    @JsonBackReference(value = "user_fk")
    @Comment("그룹 정보")
    private List<UserGroup> userGroupList;
    ...
}
```

```java
@Entity
public class UserGroup {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonManagedReference(value = "group_fk")
    @Comment("그룹 정보 FK")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonManagedReference(value = "user_fk")
    @Comment("유저 정보 FK")
    private User user;
    
    @NotNull
    @Comment("상태값")
    private Boolean status;
}
```
와 같이 데이터 테이블을 생성해주면 해결된다.