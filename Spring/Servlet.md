### **Spring Container? Servlet Container**?
#### Servlet이란?
- Servlet은 Java EE의 표준 중 하나로 javax.servlet Package를 기반으로 Server에서 동작하는 Class들을 의미한다.
- 클라이언트의 요청을 처리하고 그 결과를 반환하는 Servlet Class의 구현 규칙을 지킨 자바 웹 프로그래밍 기술이다. 웹 서버의 성능을 향상하기 위해 사용되는 자바 클래스의 일종이다.
- JSP와 비슷한 점이 있지만, JSP는 HTML 코드에 Java 코드를 포함하고 있는 반면 Servlet은 **자바 코드 안에 HTML 코드를 포함하고 있다**는 차이점이 있다.

> **Servlet** 간단 정리 <br>
> - 클라이언트의 요청에 대해 동적으로 작동하는 웹 어플리케이션 컴포넌트
> - `HTML`을 사용하여 요청에 응답한다.
> - `Java Thread`를 이용하여 동작한다
> - MVC 패턴에서 Controller로 이용된다.
> - HTTP 프로토콜 서비스를 지원하는 `javax.servlet.http.HttpServlet` 클래스를 상속받는다.

토큰 정보를 받기위해 사용하던 `HttpServletRequest`는 Http 프로토콜의 request 정보를 Servlet에게 전달하기 위한 목적으로 사용되는 Class이며
Header정보, Parameter, Cookie, URI, URL 등의 정보를 읽어들이는 메소드를 가지고 있다. 

### Servlet 동작 방식
![img.png](img.png)

### Application Context와 Servlet Context
| Application Context                                                                                                                                                                                                                                                                                                                                                   | Servlet Context                                                                                                                                                                                                                                                                                     |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| - Web Application 최상단에 위치하고 있는 Context<br> - Spring에서 ApplicationContext란 BeanFactory를 상속받고 있는 Context -> Spring에서 생성되는 Bean에 대한 IoC Container<br> - 특정 Servlet설정과 관계 없는 설정을 한다 (@Service, @Repository, @Configuration, @Component) <br>- 서로 다른 여러 Servlet에서 공통적으로 공유해서 사용할 수 있는 Bean을 선언한다.<br> Application Context에 정의된 Bean은 Servlet Context에 정의 된 Bean을 사용할 수 없다. | - Servlet 단위로 생성되는 Context<br> - 하나의 Servlet이 Servlet Container와 통신하기 위해서 사용되어지는 메서드들을 가지고 있는 클래스가 바로 ServletContext<br> - Spring에서 servlet-context.xml 파일은 DispatcherServlet 생성 시에 필요한 설정 정보를 담은 파일 (Interceptor, Bean생성, ViewResolver등..)<br> - URL설정이 있는 Bean을 생성 (@Controller, Interceptor)<br> |

> ### Application Context와 Servlet Context 간단 정리
> **Application Context**
> - 공통 기능을 할 수 있는 Bean 설정 (Service, Util 등..)
> - 각 Servlet에서 공유할 수 있는 Bean
> 
> **Servlet Context**
> - Servlet 구성에 필요한 Bean 설정 (Controller, Interceptor, Mapping Handler 등)

### ApplicationContext와 ServletContext의 life-cycle
**RootApplicationContext**

최상위 ApplicationContext이다. WebApplicationContext의 부모 Context이며 자식에게 자신의 설정을 공유한다.

Context가 여러개의 Bean들을 포함한 여러 기능을 가진 공간이라고 했는데 RootApplicationContext에 설정된

Bean들은 모든 컨텍스트 공간에서 사용이 가능하다.

단, 자신의 자식인 WebApplicationContext의 설정에는 접근하지 못한다.

보통은 어플리케이션 모든곳에서 사용해야하는 Component-Scan, SpringSecurity 설정, DB설정 등을 설정한다.



**WebApplicationContext**

Servlet 단위의 ApplicationContext이다. RootApplicationContext의 자식 Context이며, 부모인 RootApplicationContext의

설정에 접근할 수 있다.

Spring Container
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FookR0%2Fbtrj9ksZzU9%2FdmI4beylluaIPKAMQkRXw0%2Fimg.png"></img>


참고자료: https://mangkyu.tistory.com/14