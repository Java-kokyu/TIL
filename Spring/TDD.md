#### ⭐ Spring에서의 TDD(Test-Driven Development, 테스트 주도 개발) 프로그래밍 방법 
- **Repository -> Service -> Controller** 순서로 개발을 진행한다.
- Repository 계층의 테스트는 H2와 같은 인메모리 데이터베이스 기반의 통합 테스트로 진행한다.
- Service 계층의 테스트는 Mockito를 사용해 Repository 계층을 Mock하여 진행한다.
- Controller 계층의 테스트는 SpringTest의 MockMvc를 사용해 진행한다.