package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        // close를 제공하지 않음 ApplicationContext는! ConfigurableApplicationContext 구현체는 제공한다.
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();

        // 스프링 컨테이너가 개발자에게 의존관계 주입이 완료되었다는 것을 알려줘야 개발자가 객체 초기화를 할 수 있음
        // => 콜백 메서드 활용

        // 생성자에서는 메모리 할당(객체 생성) 및 정말 필요한 기본 값만 초기화 하는 정도로 하는 것이 좋다.
        // 단일 책임 원칙을 생각하자!
        // 초기화에서는 보통 디비 커넥션과 같은 무거운 동작을 수행함
        // 나누는 것이 훨씬 더 유지보수 관점에서 좋다.

        // 나누면 lazy init을 할 수 있음(최초의 접근 때 초기화)
    }

    @Configuration
    static class LifeCycleConfig {

        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
