package hello.core.singletone;

// 1. 객체를 미리 생성해두는 가장 단순하고 안전한 방법 => 선택된 방법
// 2. 객체를 지연하여 사용될 때 생성하는 방법

public class SingletoneService {

    private static final SingletoneService instance = new SingletoneService();

    public static SingletoneService getInstance() {
        return instance;
    }

    // 외부에서 객체 생성 방지
    private SingletoneService() {
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }

}
