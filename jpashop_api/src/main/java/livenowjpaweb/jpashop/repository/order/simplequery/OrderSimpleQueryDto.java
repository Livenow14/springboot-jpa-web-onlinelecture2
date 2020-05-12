package livenowjpaweb.jpashop.repository.order.simplequery;

import livenowjpaweb.jpashop.domain.Address;
import livenowjpaweb.jpashop.domain.Order;
import livenowjpaweb.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
            this. orderId = orderId;
            this. name = name;
            this. orderDate = orderDate;
            this. orderStatus = orderStatus;
            this. address  =address;
        }
}

