/*
package livenowjpaweb.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos{
        List<OrderQueryDto> result = findOrders();

        result.forEach(o-> {
                    List<OrderItemQueryDto> orderItem = findOrderItems(o.getOrderId());
                }
        );
    }

*/
/*    private List<OrderItemQueryDto> findOrderItems(Long orderId){
        return em.createQuery()

    }*//*


    public List<OrderQueryDto> findOrders() {       //새로 만든이유는, repo가 controller를 참조하는 순환관계가 설정됨
        em.createQuery(" select new livenowjpaweb.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +           //이렇게 하면 컬렉션이 안되지만 해야한다
                " from Order o " +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
*/
