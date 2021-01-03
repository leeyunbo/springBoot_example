package hello.core.singletone;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository repository = ac.getBean("memberRepository", MemberRepository.class);

        // MemberRepository memberRepository1 = memberService.getMemberRepository();
        // MemberRepository memberRepository2 = orderService.getMemberRepository();
        // repository

        // 위에 세 개는 같다. 왜 같지? => 무조건 싱글톤을 보장해주는구나.
        // 스프링은 바이트코드를 조작하는 라이브러리를 사용한다.


    }

    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean.getClass() = " + bean.getClass());
        // bean.getClass() = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$4e1a4a11 ??
        // 원래라면 class hello.core.AppConfig.class 가 출력 되어야하는데?
        // CGLIB 바이트코드 조작 라이브러리를 사용하여 AppConfig.class 상속받은 임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록한 것. (뭔가 싱글톤을 유지하도록 로직이 있겠지?)
        // Configuration을 빼버리면? CGLIB 로직이 빠지게 되어 순수 AppConfig.class가 출력됨 (싱글톤이 보장이 안됨)
        // 스프링 빈에 등록할 때 한번 호출 되고, 코드 상에서 new 키워드를 호출할 때마다 호출 됨 => 개발자가 스스로 호출한거랑 다를게 없음
    }
}
