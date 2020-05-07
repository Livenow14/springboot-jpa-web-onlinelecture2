package livenowjpaweb.jpashop;

import livenowjpaweb.jpashop.domain.*;
import livenowjpaweb.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/** 총 주문 2개
 *userA
 * JPA1 BOOK
 * JPA2 BOOK
 * userB
 * SPRING1 BOOK
 * SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class initDb {
    //조회용 샘플 데이터 -> 매번 치기 힘들다.
    private final InitService initService;

    @PostConstruct                  //스프링빈이 다올라가면 스프링이 이것을 호출을 해줌, 별로도 메서르를 빼야지 Transactional이 먹음
    public void init(){
        initService.dbInit1();
        initService.dbInit2();

    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {                         //ctrl + alt + m 하면 craeet 메소드를 해줌
            Member member = createMember("userA", "서울", "1", "111");
            em.persist(member);

            Book book = createBook("JPA BOOK1", 10000, 100);
            em.persist(book);

            Book book2 = createBook("JPA BOOK2", 20000, 100);
            em.persist(book2);

            OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem, orderItem2);//OrderItem...에서 이걸헤 사용할 수 있게 하였음.
            em.persist(order);
        }
        public void dbInit2() {                         //ctrl + alt + m 하면 craeet 메소드를 해줌
            Member member = createMember("userB", "진주", "2", "333");
            em.persist(member);

            Book book = createBook("SPRING BOOK1", 20000, 200);
            em.persist(book);

            Book book2 = createBook("SPRING BOOK2", 40000, 300);
            em.persist(book2);

            OrderItem orderItem = OrderItem.createOrderItem(book, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem, orderItem2);//OrderItem...에서 이걸헤 사용할 수 있게 하였음.
            em.persist(order);
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book createBook(String name, int price, int stockQuantity) {               //shift + f6은 rename
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);                                   //ctrl + alt + p 는 파라미터화
            return book;
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }



    }
}
