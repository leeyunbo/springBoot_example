package hello.core.order;

//public이면 외부에 공개하는 것이기 때문에 외부 파일이름과 같아야함
public interface OrderService {
    Order createOrder(Long member, String itemName, int itemPrice);
}
