package livenowjpaweb.jpashop.api;

import livenowjpaweb.jpashop.domain.Order;
import livenowjpaweb.jpashop.repository.OrderRepository;
import livenowjpaweb.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/api/v1/simple-orders")                                                            //무한루프에 빠지는 예제  이거는 따로하지않는다.
    public List<Order> orderV1(){                                                                  // 이렇게 하면 절대 안되기때문에 진행하지 않는다.
        List<Order> all = orderRepository.findAllByString(new OrderSearch());                       //DTO로 변환해서 사용하는게 좋다.
        return all;

    }


}
