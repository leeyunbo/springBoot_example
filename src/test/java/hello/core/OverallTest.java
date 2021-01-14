package hello.core;

import hello.core.member.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OverallTest {


    @Test
    void getMemberInform() {
//        ApplicationContext ac = new AnnotationConfigApplicationContext(CoreApplication.class);
//        MemberService memberService = ac.getBean(MemberServiceImpl.class);
//        memberService.join(new Member(20L, "name", Grade.VIP));
//        Member member = memberService.findMember(20L);
//
//        Assertions.assertThat(member.getName()).isEqualTo("name'");

    }

}
