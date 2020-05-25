package livenowjpaweb.jpashop.service.query;

import livenowjpaweb.jpashop.api.OrderApiController;
import livenowjpaweb.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;
/**
 * OSIN 를 끄고 이렇게 service를 분리하여 한다.
 * 핵심 비지니스는 OrderService에 담고
 * 화면이나 API에 맞춤 서비스(주리 읽기 전용 트랜잭션)은 OrderQueryService에 담는다.
 */


/*
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    public List<OrderApiController.OrderDto> ordersV3() {
        List<Order> orders = orderRepository.finAllWithItem();
        for (Order order : orders) {
            System.out.println("order = " + order + " id=" + order.getId());   //distinct  전에는 4개가 나온다
        }

        List<OrderApiController.OrderDto> result = orders.stream()
                .map(o -> new OrderApiController.OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }
}
*/
