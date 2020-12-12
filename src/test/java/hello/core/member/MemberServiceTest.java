package hello.core.member;

/*
 * MemberService 테스트 코드
 */

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService = new MemberServiceImpl();

    @Test
    void join() {
        //given, 이러한 것이 주어졌을떄
        Member member = new Member(1L, "memberA", Grade.VIP);

        //when, 이러한 것을 하면
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then, 이렇게 된다
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
