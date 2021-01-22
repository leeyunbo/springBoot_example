package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);


        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2  = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(1);


    }

    @Scope("singleton")
    // 싱글톤이니까, 한번 주입된 이후에 절대 안바뀌지 왜냐? ClientBean이 하나뿐이니까 계속 함께 유지됨
    // 클라이언트A, 클라이언트B가 있다면 내부에 있는 prototypeBean은 다른놈들임
    static class ClientBean {




        @Autowired
        //ObjectProvider가 ObjectFactory의 자식이다. Spring이 제공한다. 따라서 Spring에 의존적이다.
        //Dependency Lookup (DL)
        //private ObjectProvider <PrototypeBean> prototypeBeanProvider;
        //private ObjectFactory<PrototypeBean> prototypeBeanProvider;


        //Spring이 아닌 자바 표준 라이브러리라, 그래들에 추가해줘야한다.
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }


    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        // 얘네는 자바 표준이지만, 기능이 너무 좋아서 스프링에서도 권장함
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
