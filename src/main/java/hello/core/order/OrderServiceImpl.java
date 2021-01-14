package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    // ac.getBean(MemberRepository.class) => 타입이 같으면?????
    // 1. Autowired는 타입 매칭 시도 후, 중복이면 파라미터 이름으로 매칭 시도 함(필드명 or 롬북 사용안하면 생성자 인자 명)
    // 2. @Qualifier("이름") => 빈이름이 변경되는 것은 아니고, 구분자를 붙여주는 느낌임 (각각 중복될 수 있는 빈 위에 Qualifier을 통해 특수한 이름을 부여한다.) => 매칭 시키는 느낌으로 사용하는 것이 좋
    // 3. @Primary => 우선순위를 정하며, 여러 빈이 매칭되면 @Primary를 가진 빈이 우선순위를 가짐

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        //멤버 등급을 체킹하기 위한 멤버 정보 호출
        Member member = memberRepository.findById(memberId);

        //할인 정책이 변경되어도 OrderService는 변경될 필요가 없음 (단일 책임의 원칙)
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
