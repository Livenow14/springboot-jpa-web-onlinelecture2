package livenowjpaweb.jpashop.service;

import livenowjpaweb.jpashop.domain.Delivery;
import livenowjpaweb.jpashop.domain.Member;
import livenowjpaweb.jpashop.domain.Order;
import livenowjpaweb.jpashop.domain.OrderItem;
import livenowjpaweb.jpashop.domain.item.Item;
import livenowjpaweb.jpashop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count ){

        // 엔티티 조회
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findOne(itemId);

        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성   셍상 메서드를 사용해보자
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);  // 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴이라함.

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);     // 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴이라함.

        //주문 저장
        orderRepository.save(order);        //이전에 cascade를 해줬기 때문에 이것만 해줘도 orderitem 랑 delivery가 자동으로 persist가됨
                                        //cascade를  언제써야할가? 주인이 private owner일 때만
        return  order.getId();          //lifecycle이 같이 persist를 할때 주로 씀

    }

    /**
     * 주문취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();;
    }

    //검색
  public List<Order> findOrders(OrderSearch orderSearch){
        //return orderRepository.findAllByString(orderSearch);
        return orderRepository.findAllDsl(orderSearch);
    }
}
