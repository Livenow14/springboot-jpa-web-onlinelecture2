package livenowjpaweb.jpashop.api;


import livenowjpaweb.jpashop.domain.Address;
import livenowjpaweb.jpashop.domain.Order;
import livenowjpaweb.jpashop.domain.OrderItem;
import livenowjpaweb.jpashop.domain.OrderStatus;
import livenowjpaweb.jpashop.repository.OrderRepository;
import livenowjpaweb.jpashop.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 1대다, 다대다일 때의 조회를 위한 api
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    /**
     * V1. 엔티티 직접 노출
     * - 엔티티가 변하면 API 스펙이 변한다.
     * - 트랜잭션 안에서 지연 로딩 필요
     * - 양방향 연관
     *
     */
/*    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all){

        }

    }*/             //hibernate5를 사용, 엔테테를 직접 노출하는 것은 좋지않기에 안함.

     /** V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 트랜잭션 안에서 지연 로딩 필요
     * - 쿼리가 굉장히 많이 나옴
     */
     @GetMapping("/api/v2/orders")
     public List<OrderDto> orderV2(){
         List<Order> orders = orderRepository.findAllByString(new OrderSearch());
         List<OrderDto> collect = orders.stream()
                 .map(o -> new OrderDto(o))
                 .collect(Collectors.toList());
            return collect;
     }

     @Getter
     static class OrderDto {

         private Long orderId;
         private String name;
         private LocalDateTime orderDate;
         private OrderStatus orderStatus;
         private Address address;
         private List<OrderItemDto> orderItems;        //Dto 안에 엔티티가 있으면 안된다.


         public OrderDto(Order order) {
             orderId = order.getId();
             name = order.getMember().getName();
             orderDate= order.getOrderDate();
             orderStatus= order.getStatus();
             address = order.getDelivery().getAddress();
                orderItems = order.getOrderItems().stream()
                        .map(orderItem -> new OrderItemDto(orderItem))
                        .collect(Collectors.toList());
         }
     }

     @Getter
    static class OrderItemDto{
         private String itemName;       //상풍명
         private int orderPrice;        //주문 가격
         private int count;             //주문 수량

         public OrderItemDto(OrderItem orderItem) {
             itemName = orderItem.getItem().getName();
             orderPrice = orderItem.getOrderPrice();
             count = orderItem.getCount();

         }
     }



     /** V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O) (컬렉션 패치 조인)
     * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size?
      *
      * 옵션 주면 N -> 1 쿼리로 변경가능)
     */

     @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
         List<Order> orders = orderRepository.finAllWithItem();
         for (Order order : orders) {
             System.out.println("order = " + order + " id=" + order.getId());   //distinct  전에는 4개가 나온다
         }

         List<OrderDto> result = orders.stream()
                 .map(o -> new OrderDto(o))
                 .collect(Collectors.toList());
         return result;
     }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Order> orders = orderRepository.finAllWithMemberDelivery(offset,limit);

        for (Order order : orders) {
            System.out.println("order = " + order + " id=" + order.getId());   //distinct  전에는 4개가 나온다
        }

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }


     /** V4. JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + N Query)
     *  - 페이징 가능
     *  V5. JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
     * - 페이징 가능
     * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
     * - 페이징 불가능...
     *
    */


}
