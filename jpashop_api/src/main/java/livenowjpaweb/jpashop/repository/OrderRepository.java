package livenowjpaweb.jpashop.repository;

import livenowjpaweb.jpashop.domain.Member;
import livenowjpaweb.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order); //영속성 컨텍스트에 order객체를 넣음 transaction이 커밋되는 시점에 디비에 반영됨
    }
    public Order findOne(Long id){
        return em.find(Order.class, id );
    }





  public List<Order> findAll(OrderSearch orderSearch){
        String jpql ="select  o from Order o join o.member m ";

        return em.createQuery("select o from Order o join o.member m " +
                " where o.status = :status" +
                " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)        //재한을 걸어줌
                .getResultList();     //이건 정적 쿼리, 값을 알고있기 때문에


    }


    /**
     * JPQL로 처리, 쿼리를 문자로 생성하기는 번거롭고, 실수로 인한 버그가 충분히 발생할 수 있다.
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        } TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /**
     * JPA Criteria는 JPA 표준 스펙이지만 실무에서 사용하기에 너무 복잡하다. 결국 다른 대안이 필요하다. 많
     * 은 개발자가 비슷한 고민을 했지만, 가장 멋진 해결책은 Querydsl이 제시했다. Querydsl 소개장에서 간
     * 단히 언급하겠다. 지금은 이대로 진행하자.
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) { //실무에 쓰라고 만든게 아님 그래서 그냥 넘어감.
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name); }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대  1000건

        return query.getResultList();
    }

    public List<Order> finAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o"+
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    /**
     * distinct의 두개지 기능 db에 distinct를 날려주고, entity가 중복인 경우 걸러서 날려줌.
     * 쿼리가 1번으로 줄어듬.
     * 하지만 단점이 크다. 페이징이 불가하다.
     */
    public List<Order> finAllWithItem() {
        return em.createQuery(" select distinct  o from Order o"+
                " join fetch o.member m" +
                " join fetch o.delivery d"+
                " join fetch  o.orderItems oi"+
                " join fetch oi.item i", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)                         //이게 먹지않음 양이 많을때 페이징을 하면안됨 패치 조인일때.
                .getResultList();                           //메모리에서 페이징을 하는데(이는 매우 위험)
                                                            // fetch조인에선 컬랙션 조회는 1개만 사용가능
    }

    public List<Order> finAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o"+
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).setFirstResult(offset)
         .setMaxResults(limit)
         .getResultList();
    }
}
