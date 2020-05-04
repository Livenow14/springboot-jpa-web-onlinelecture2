package livenowjpaweb.jpashop.controller;

import livenowjpaweb.jpashop.domain.Member;
import livenowjpaweb.jpashop.domain.Order;
import livenowjpaweb.jpashop.domain.item.Item;
import livenowjpaweb.jpashop.repository.OrderSearch;
import livenowjpaweb.jpashop.service.ItemService;
import livenowjpaweb.jpashop.service.MemberService;
import livenowjpaweb.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String creatForm(Model model){

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,    //파라미터를 memberId로해서 받았다
                        @RequestParam("itemId") Long itemId,        //RequestParam은 templates의 select옵션의 memberId나
                        @RequestParam("count") int count){          //ItemId의 선택된 value가 submit으로 넘어오게 되는것

        orderService.order(memberId, itemId, count);    //이러면 order로직이 돌아감, 이렇게 비지니스 로직으로 보내게 짜는게 좋다.
        return "redirect:/orders";                      //여려개 상품을 선택할 수 있게 해보자!



    }
    /**
     * 페이지에서 검색 or 주문상태를 선택후 검색을 하면 ModelAttribue에 의해 orderSearch가 담기고
     * 이 담긴 것들을 밑의 코드의 orders를 이용해서 페이지 밑에 표시하여 준다.
     */

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){ //@ModelAttribute는  model box에 자동으로 담김, 뿌릴 수도 있고 담을 수 도있다. orderList에서 주문상태로 뿌려준건 받으면 그에 맞는 데이터가 출력.
        List<Order> orders = orderService.findOrders(orderSearch);//조회는 여기서 바로 레포지토리를 사용해도 문제없다. 예제 에서는 로직이 간단하니 그냥 위임하도록한것임
        model.addAttribute("orders",orders);
    //    model.addAttribute("orderSearch",orderSearch); //이부분이 @ModelAttribute로 인해 생략됨
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
