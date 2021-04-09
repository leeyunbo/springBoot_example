# SpringBoot를 활용한 간단한 예제, 객체 지향 극대화
1. 멤버 등급에 따른 할인 정책 적용 
2. 멤버 계정 추가
3. 주문 객체 생성 

### 첫번째 문제(순수 JAVA를 이용한 구현) 
```java
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);

        //할인 정책이 변경되어도 OrderService는 변경될 필요가 없음 (단일 책임의 원칙)
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
```
Q. `RateDiscountPolicy`(정액 할인법)과 `FixDiscountPolicy`(정률 할인법)을 구현한 후, 필요할때 마다 변경할 수 있도록 역할과 구현을 확실하게 분리하여 설계를 진행하였다. 그렇다면 OCP와 DIP를 지키게 된걸까? <br/>
A. `OrderServiceImpl`에서 코드의 변경이 이루어질 뿐만 아니라(OCP 위반), `OrderServiceImpl`이 인터페이스인 `DiscountPolicy`, 구현 객체인 `RateDiscountPolicy`에 동시에 의존(DIP 위반)한다. 따라서 OCP, DIP를 지킨다고 할 수 없다.

Q. 해결 방법은? <br/>
A. `OrderServiceImpl`이 DIP를 지키기 위해서는 인터페이스(추상화)에만 의존해야 한다. 따라서 구현 객체를 생성하고 주입시켜줄 수 있는 제 3자가 필요하다.

<br/>

### 두번째 문제(관심사 분리) 
```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
```
1. 위의 예시에서 OCP, DIP를 지키기 위해서는 구현 객체를 생성하고 주입시켜주는 관심사만 가지고 있는 제 3자가 필요하다고 했다. 
2. 따라서 구현 객체를 생성하고 생성자를 통해 주입시켜주는 관심사에만 관심이 있는 `AppConfig`를 구현하였다. 
3. 이를 의존관계를 마치 외부에서 주입해주는 것 같다고 해서 `DI` 우리말로 `의존관계 주입` 또는 `의존성 주입`이라 한다.
3. `AppConfig`덕분에 `OrderServiceImpl`은 단순히 주문 정보만 얻어오는 관심사만 가질 수 있게 되었다. 
4. `OrderServiceImpl`은 `OrderService` 인터페이스에만 의존하기 때문에 DIP를 만족한다고 할 수 있다. 

#### SRP도 만족할까?
1. `AppConfig`로 분리하기 전에 테스트에 활용했던 `OrderApp` 클라이언트나 `MemberApp` 클라이언트는 객체를 생성, 실행하는 다양한 책임을 가지고 있었다.
2. 하지만 `AppConfig`로 분리하게 되면서, `AppConfig` 객체 생성의 책임을 담당하고 클라이언트들이 객체 실행의 책임만을 담당하게 되었다.
3. 따라서 `AppConfig`로 분리하게 되면서, SRP도 만족하게 되었음을 의미한다.

<br/>

### 세번째 문제(AppConfig 리팩토링)
```java
public class AppConfig {
    
    // MemberService를 생성하는 역할
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    // MemberRepository를 생성하는 역할
    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    // OrderService를 생성하는 역할
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    // DiscountPolicy를 생성하는 역할
    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }
}
```

1. 원래는 `OrderService`가 `DiscountPolicy`를 생성하고, `MemberService`가 `MemberRepositry`를 생성하는 등 역할이 겹쳐져 있었다.
2. 그렇기에 코드의 중복이 생기고 전체가 어떻게 구성되어 있는지 파악하기 어려웠으며, 변경하기에 굉장히 복잡하였다.
3. 하지만 각각 역할을 명확하게 나타나도록 구현함으로써 문제를 해결할 수 있었다.
4. 예시와 같이 역할을 명확하게 나타나도록 구현하게 되면 전체적인 구성이 눈에 쉽게 들어오며, 변경이 용이하고, 중복이 일어나지 않게 된다.

<br/>

### 네번째 문제(AppConfig 할인 정책 변경)
```java
public DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy();
    return new RateDiscountPolicy();
}
```
1. [첫번째 문제](https://github.com/leeyunbo/B2CBackend#%EC%B2%AB%EB%B2%88%EC%A7%B8-%EB%AC%B8%EC%A0%9C%EC%88%9C%EC%88%98-java%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EA%B5%AC%ED%98%84)에서 할인 정책이 변경되면 `OrderServiceImpl`이 변경되어야 하기에 OCP를 만족하지 않는다고 했었다. 
2. 또한 `OrderServiceImpl`이 구현 객체에 의존하기에 DIP도 만족하지 않는다고 하였다.
3. 따라서 객체를 생성하고 의존성을 주입해주는 `AppConfig`를 구현하였다.
4. 만약 현 상황에서 할인 정책이 변경되면 단순히 `AppConfig`의 `discountPolicy()` 메서드만 변경함으로써 기능을 변경할 수 있다.

#### 구성 영역과 사용 영역
![스크린샷 2020-12-17 오후 9 59 56](https://user-images.githubusercontent.com/44944031/102491261-45d76600-40b3-11eb-8469-818e49848c2f.png)
1. 다음과 같이 `AppConfig` 구성 영역만 영향을 받고, 사용 영역은 전혀 영향을 받지 않는다.
2. 물론 `구성 영역`은 당연히 변경된다. `AppConfig`는 공연의 기획자로서 공연 참여자인 구현 객체들을 모두 알아야 한다.
3. 따라서 코드의 변경이 이루어지지 않고, 각각 구성 요소들이 인터페이스에만 의존하므로 OCP와 DIP 모두를 만족한다.

<br/>

### 다섯번째 문제(Spring Framework 적용)
```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
```
1. 다음과 같이 `AppConfig`를 Spring을 활용하여 수정하면 다음과 같다.
2. 스프링 컨테이너는 `@Configuration`이 붙은 `AppConfig`를 설정 정보로 사용한다.
3. `@Bean`이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. (이들을 `스프링 빈`이라 한다.) 
4. 스프링 빈은 `@Bean`이 붙은 메서드 명을 스프링 빈의 이름으로 사용한다. (memberService, orderService...) 

#### 클라이언트는 어떻게 변경될까? 
```java
public class OrderApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);
        
        System.out.println("order = " + order);
    }
}
```
1. `ApplicationContext`를 스프링 컨테이너라 한다.
2. 이제부터는 `스프링 컨테이너`를 통해 객체를 직접 생성하고 DI 한다.
3. 이전에는 필요한 객체를 `AppConfig`를 사용해서 직접 호출했지만, 이제는 스프링 컨테이너를 통하여 찾아야 한다. 
4. 스프링 빈은 `applicationContext.getBean()` 메서드를 사용하여 찾을 수 있다.
5. 기존에는 개발자가 직접 자바 코드로 모든 것을 했다면 이제는 스프링 컨테이너에 객체를 스프링 빈으로 등록하고, 스프링 컨테이너에서 찾아 사용하도록 변경되었다. 

