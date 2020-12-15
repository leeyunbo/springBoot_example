package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    // 할인 정책을 변경하려면 OrderServiceImpl 코드를 고쳐야 한다.
    // 역할과 구현을 확실하게 분리했지만
    // DIP를 지키지 못함(DiscountPolicy 인터페이 뿐만 아니라, RateDiscountPolicy 구현 객체에도 의존해버림)
    // OCP를 지키지 못함(OrderServiceImpl 코드의 변경이 이루어져버림)
    // 누가 대신 구현 객체를 생성하여 주입 시켜준다면? 이 문제가 해결됨!
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        //멤버 등급을 체킹하기 위한 멤버 정보 호출
        Member member = memberRepository.findById(memberId);

        //할인 정책이 변경되어도 OrderService는 변경될 필요가 없음 (단일 책임의 원칙)
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
