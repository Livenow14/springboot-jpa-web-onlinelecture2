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
/*    private final OrderQueryRepository orderQueryRepository;*/

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
     */
/*
     @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
         return orderQueryRepository.findOrders();

     }
*/




     /**  V5. JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
     * - 페이징 가능
     * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
     * - 페이징 불가능...
     * 대부분의 어플리케이션에서는 fetch join으로 해결된다. 이를토해 해결되지 않으면
      * cache를 이용하거나 하지 DTO로 직접조회를 사용하진 않는다.
      * 인티티는 직접 캐싱하면 안된다.
    */

    /**
     * OSIV
     * open session in view
     * open EntityManager in view
     *
     * default가 true이다.
     * - 모든 상태에서 영속성 컨텍스트 생존번위이다.
     * 오랜시간동안 데이터베이스 커넥션 리소스를 사용하기 때문에, 실시간 트래픽이 중요한
     * 애플리케이션에서는 커넥션이 모자랄 수 있어 장애로 이어진다.
     *
     *
     * open-in-view: false
     * - 이를 false로 하면 종료되는데, 트렌잭션을 종료할 때 영속성 컨텍스트를 닫고,
     * 데이터베이스 커넥션도 반환한다. 따라서 커넥션 리소를 낭비하지 않는다.
     * service와 repository에서만 영속성 컨텍스트생존 번위이다. 그렇기 때문에
     * controller view, filter intercptor에서는 영속성 컨텍스트를 사용할 수 없다.(준영속 상태)
     *
     * 끈 상태로 복잡성을 관리하려면 Command와 Query를 분리한다.
     *
     * OderServkce: 핵심 비즈니스 로직
     * OrderQueryService : 화면이나 API에 맞춘 서비스(주로 읽기 전용 트랜잭션 사용)
     *
     * 참고; 고객 서비스와 실시간 API는 OSIV를 끄고, ADMIN 처럼 커넥셔늘 많이 사용하지
     * 않는 곳에서는 OSIV를 켠다.
     * 자세한건 pdf파일 확인.
     */


}
