package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class FixDiscountPolicy implements DiscountPolicy{

    private int discountFixAmout = 1000; // 1000원 정액 할인

    @Override
    public int discount(Member member, int price) {
        // enum 타입은 == 쓰는게 맞음
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmout;
        }
        else {
            return 0;
        }
    }
}
