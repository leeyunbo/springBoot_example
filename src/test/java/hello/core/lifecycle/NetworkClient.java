package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient  {

    private String url;


    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String msg) {
        System.out.println("call: " + url + " message = " + msg);
    }

    //서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }


    /*
     * 스프링 코드에 의존하지 않음
     * 이름을 자유자재로 지을 수 있음
     * 코드가 아니라 설정 정보를 사용하기에 코드를 고칠 수 없는 외부 라이브러리에도 적용할 수 있음
     */
    // 빈이 생성된 후 호출되는 메서드
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init()");
        connect();
        call("초기화 연결 메시지");
    }

    // 빈이 파괴될 때 호출되는 메서드
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close()");
        disconnect();
    }






    /*
     * 코드 레벨에서 사용하는 인터페이스이기 때문에, 내가 고칠 수 없는 라이브러리에는 적용할 수 없음 (클래스 파일로 가져올텐데..)
     * 초기화, 소멸 메서드의 이름을 변경할 수 없다.
     * 모든 코드가 스프링 전용 인터페이스에 의존적으로 설계해야 함
     */
    // Properties의 셋팅이 끝나면 호출되는 메서드
/*    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet()");
        connect();
        call("초기화 연결 메시지");
    }

    // 빈이 파괴될 때 호출되는 메서드
    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destory()");
        disconnect();
    }*/
}
