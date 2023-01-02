### **SOLID**
- SRP(Single Responsibility Principle): 단일 책임 원칙
- OCP(Open/Closed Principle): 개방/폐쇄 원칙
- LSP(Liskov Substitution Principle): 리스코프 치환 원칙
- ISP(Interface Segregation Principle): 인터페이스 분리 원칙
- DIP(Dependency Inversion Principle): 의존관계 역전 원칙

### 1. SRP(Single Responsibility Principle)
- 하나의 클래스는 하나의 **책임**만 가져야 한다.
- 책임의 범위를 정할 때, 가장 중요한 지표가 되는 것은 **변경**이다. 변경이 있을 때 파급 효과가 적으면 단일 책임 원칙을 잘 따른 것이다.

### **2. OCP(Open/Closed Principle)**
- 소프트웨어 요소는 **확장에는 열려있으나 변경에는 닫혀 있어야 한다.**
- 다형성(Polymorphism)을 활용하는 것이 핵심이다. **(역할과 구현의 분리.)**
- Interface를 구현한 새로운 Class를 생성하여 새로운 기능을 구현한다.

```java
public class MemberService {
    MemberRepository memberRepository = new MemoryRepository();
    MemberRepository memberRepository = new JDBCRepository();
}
```
위 코드의 문제점은 `MemoryRepository`든 `JDBCRepository`든, 결국 `MemberService` 클라이언트가 구현 클래스를 직접 선택하는 방식을 택하고 있다. Interface를 선언하고 이를 구현한 Class를 활용하여 다형성을 사용했는데도, OCP 원칙은 지킬 수 없다. `memberRepository`를 변경하려면 `MemberService`의 코드를 변경해야하기 때문이다. 따라서 OCP 개방 폐쇄 원칙을 지키기 위해서는 객체를 생성하고 연관관계를 맺어주는 별도의 조립, 설정자(Spring Container)가 필요하다.

### 3. LSP(Liskov Substitution Principle)
- 다형성에서 하위 Class는 Interface의 규약을 다 지켜야 한다는 것. 단순히 컴파일 성공을 넘어서, 프로그램의 정확성을 깨뜨리지 않아야 한다는 원칙이다.
- 자동차 Interface의 엑셀은 앞으로 가능 기능이라고 규정지었다면, 자동차 구현체(객체)에서 엑셀을 뒤로 가게 구현하면 LSP 위반이다.

### 4. ISP(Interface Segregation Principle)
- 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.

### 5. **DIP(Dependency Inversion Principle)**
- 프로그래머는 **추상화에 의존해야지, 구체화에 의존하면 안된다.**. 즉, **구현 클래스에 의존하지 말고, 인터페이스에 의존해야한다**
- **역할(ROLE)에 의존하게 해야한다.** 클라이언트가 구현체에 의존하게 되면 변경이 어려워진다. 따라서 **클라이언트가 인터페이스에 의존**하게 해야한다.

```java
public class MemberService {
    MemberRepository memberRepository = new MemoryRepository();
    MemberRepository memberRepository = new JDBCRepository();
}
```
위 코드에서 MemberService는 MemberRepository라는 인터페이스에 의존하지만 MemoryRepository, JDBCRepository와 같은 구현체에도 의존한다. MemberRepository가 구현체에 의존하기 때문이다. 위는 DIP/OCP 원칙 모두 위반한 코드가 되는 것이다.

### 정리
객체 지향의 핵심은 다형성이며, 다형성을 이용하면 부품을 갈아 끼우듯이 개발을 할 수 있게 된다. 하지만 다형성만으로는 OCP와 DIP를 지킬 수 없다.<br>
Spring의 DI Container가 필요한 이유가 이러한 이유이다. 스프링은 DI(Dependancy Inject, 의존관계, 의존성 주입)을 통해 다형성 뿐만 아니라 OCP, DIP를 지킬 수 있게 도와준다. 즉, **클라이언트의 코드의 변경 없이 기능을 확장**한다.