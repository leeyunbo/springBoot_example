package hello.core.singletone;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        //ThreadA : A사용자가 10000원 주문
        statefulService2.order("userA", 10000);

        //ThreadB : B사용자가 20000원 주문
        statefulService.order("userB", 20000);

        //ThreadA : 사용자A 주문 금액 조회
        int price = statefulService.getPrice();
        System.out.println("price = " + price);

        // B사용자가 중간에 끼어들어서 기대하던 값이 나오지 않음 (같은 객체를 활용하다보니..)

        Assertions.assertThat(statefulService.getPrice()).isEqualTo(10000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }

    }
}