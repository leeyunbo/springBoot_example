package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 역할들을 잘 들어나게 구현하는 것이 중요하다. => 하나의 메서드가 하나의 역할을 가지고 있어야 함
 => 전체가 어떻게 구성되어 있는지 보기 쉬움, 변경하기 쉬움, 중복이 제거됨
 */

@Configuration
public class AppConfig {

    // command + option + m => 리팩토링
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

    // 역할을 잘 분리함으로써 쉽게 변경이 가능함(할인 정책이 바뀌는 경우).. 눈에 쉽게 들어옴
    @Bean
    public DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
