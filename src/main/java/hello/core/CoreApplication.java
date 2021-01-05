package hello.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// 까보면 내부에 @ComponentScan이 있음.. 따라서 스프링 부트를 사용하게 되면 솔직히 @ComponentScan을 사용할 필요가 없긴 함
@SpringBootApplication
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
