package livenowjpaweb.jpashop.api;

import livenowjpaweb.jpashop.domain.Address;
import livenowjpaweb.jpashop.domain.Order;
import livenowjpaweb.jpashop.domain.OrderStatus;
import livenowjpaweb.jpashop.repository.OrderRepository;
import livenowjpaweb.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * x To One 관계(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */


@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    /**
     * V1, 엔티티를 직접 호출
     * @return
     */

/*    @GetMapping("/api/v1/simple-orders")                                                            //무한루프에 빠지는 예제  이거는 따로하지않는다.
    public List<Order> orderV1(){                                                                  // 이렇게 하면 절대 안되기때문에 진행하지 않는다.
        List<Order> all = orderRepository.findAllByString(new OrderSearch());                       //DTO로 변환해서 사용하는게 좋다.
        return all;

    }*/

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     *  - 단점: 지연로딩으로 쿼리 N번 호출
    */

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2(){
        //ORDER(N) 2개
        // N + 1 -> 1 + 회원 N + 배송 N 총 5번 호출됨
        // 지연로딩을 무조건 써야한다. 영속성 컨텍스트를 위해 같은 멤버가 주문했다면 4번으로 변함.

        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());

        return result;

    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId=order.getId();
            name = order.getMember().getName();     //LAZY 초기화됨
            orderDate = order.getOrderDate();
            orderStatus= order.getStatus();
            address = order.getDelivery().getAddress();     //LAZY 초기화됨


        }
    }
    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - fetch join으로 쿼리 1번 호출 -> 이게 굉장히 중요하다.
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     * 패치 조인으로 order -> member, order -> delivery는 이미 조회된 상태이므로 지연로딩 x
     */

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3(){
      List<Order> orders = orderRepository.finAllWithMemberDelivery();
      return orders.stream()
              .map(o->new SimpleOrderDto(o))
              .collect(Collectors.toList());
    }


}
