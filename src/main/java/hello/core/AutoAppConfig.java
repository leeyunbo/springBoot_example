package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

// Configuration.class를 예외 시키는 이유는 이전에 만들어 놓았던 AppConfig.class, TestConfig.class 때문임. 예제를 가동시키기 위해 추가한 것
// @Configuration이 컴포넌트 스캔의 대상이 되는 이유는 내부 코드를 살펴보면 @Component가 붙어있음.

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {



}
