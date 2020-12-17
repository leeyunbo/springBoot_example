# B2CBackend
Java를 활용한 B2CBackend 개발

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

1. 원래는  OrderService가 discountPolicy를 생성하고, MemberService가 MemberRepositry를 생성하는 등 역할이 겹쳐져 있었다.
2. 그렇기에 코드의 중복이 생기고 전체가 어떻게 구성되어 있는지 파악하기 어려웠으며, 변경하기에 굉장히 복잡하였다.
3. 하지만 각각 역할을 명확하게 나타나도록 구현함으로써 문제를 해결할 수 있었다.
4. 예시와 같이 역할을 명확하게 나타나도록 구현하게 되면 전체적인 구성이 눈에 쉽게 들어오며, 변경이 용이하고, 중복이 일어나지 않게 된다.
