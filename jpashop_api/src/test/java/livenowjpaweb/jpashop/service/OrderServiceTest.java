package livenowjpaweb.jpashop.service;

import livenowjpaweb.jpashop.domain.Address;
import livenowjpaweb.jpashop.domain.Member;
import livenowjpaweb.jpashop.domain.Order;
import livenowjpaweb.jpashop.domain.OrderStatus;
import livenowjpaweb.jpashop.domain.item.Book;
import livenowjpaweb.jpashop.domain.item.Item;
import livenowjpaweb.jpashop.exception.NotEnoughStockException;
import livenowjpaweb.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional

public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("시골 jpa", 10000, 10);

        int orderCount =2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

       // Assert.assertEquals(); //alt+enter하면 스테특으로 변경해줌
       assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());    //ctrl+p를 하면 뭘 넣을지 보임
       assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
       assertEquals("주문 가격은 가격 * 수량이다.", 10000*orderCount, getOrder.getTotalPrice());
       assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity()); //expect에 10개를 넣으면 오류남
    }

 
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createBook("시골 jpa", 10000, 10);

        int orderCount = 11;
        //when
        orderService.order(member.getId(),item.getId(), orderCount);

        //then
        fail("제고 수량 부족 예외가 발생해야 한다.");  //테스트가 헷갈리면 안됨, 이것 예외가 발생하는것을 테스트 하기때문에
    }                                               //내가 만든 로직의 예외가 발생하면, 테스트가 정상 작동 한다.
                                                    //내가 만든 예외가 동작을 안하면, ex) 재고를 10개를 둔 상태에서 11개를 구매하려는데 예외가 발생안함
                                                    //테스트가 실행오류가 뜬다
                                                    //fail()= 내가만든 로직의 예외가 발생하지 않으면 이 로직이 돔 띄워줌
    @Test
    public void 주문취소() throws Exception{        //ctrl + shift + t하면 비슷한 것들끼리 왔다갔다할수있음
        //given
        Member member = createMember();
        Book item = createBook("시골 jpa", 10000, 10);

        int orderCount=2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL  이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10 , item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {        //ctrl+alt+p 하면 파라미터로 올려줌
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }


}