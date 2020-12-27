package hello.core.beandifinition;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class BeanDifinitionTest {

    // 직접 빈을 등록하거나 팩토리 빈 통해 우회해서 등록하는 방법이 있음
    // 자바 코드를 이용하여 등록하는 것이 팩토리 메서드를 통해 등록되는 방법임 (외부에서 해당 메서드를 호출하여 빈을 등록하는 방식) FactoryBean : AppConfig.class FactoryMethod : 메서드들..

    // AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    // ApplicationContext에는 Definition 관련 메서드들이 정의가 안되어있기에 구현체를 사용해야함
    GenericXmlApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");


    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("beanDefinitionsName = "  + beanDefinitionName + " beanDefinition = " + beanDefinition);
            }
        }
    }
}
