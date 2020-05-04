package livenowjpaweb.jpashop.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name ="orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //이렇게 제한하면서 짜야지, 누군가 잘못 접근할 때 제한할 수 있다.
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)  //Manythoone은 default가 Eager이기 때문에 다바꿔야함
    @JoinColumn(name="member_id")       //FK해줌
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)   //각자 persist 설정하는것을 하나로 줄여줌
    private List<OrderItem> orderItems = new ArrayList<>();

    // persist(orderItemaA)
    // persist(orderItemaB)
    // persist(orderItemaC)
    //persist(order) 를 해주는 것을 cascade를 쓰면

    //persis(order)만 쓰면됨

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)                  //엑세스를 많이하는 곳에 FK를 둠  oto 에서 어디에 두냐는 상관없음
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    //order_date로 들어감 springPhysicalNamingStrategy 하이버네이트에서 저절로 바꿔줌
    private LocalDateTime orderDate;        //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태[ORDER, CANCEL]

    //==연관관계 메서드 ==// 양방향일때 연관관계를 한번에 설정
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    //==생성 메서드 ==// //생성하는 지점을 변경하자면 여기만 바꾸면 됨
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    //==비즈니스 로직 ==//
    //주문취소

    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }

    }

    //전체 주문 가겨 조회
    public int getTotalPrice() {
/*        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;*/
        //람다나 스트림 사용 필

        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;
    }


}
