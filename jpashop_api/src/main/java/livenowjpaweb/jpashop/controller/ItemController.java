package livenowjpaweb.jpashop.controller;


import livenowjpaweb.jpashop.domain.item.Book;
import livenowjpaweb.jpashop.domain.item.Item;
import livenowjpaweb.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createFrom(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){

        Book book = new Book();                         //이것은 이후에 바꿔야한다. set을 이렇게 하는건 좋지않다.
        book.setName(form.getName());                   //실무에서는 setter를 다 랄림
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);     //"items"라는 name에 items라는 value를 넣어두고 뿌린다. 그러면
                                                             //itemList.html에서 받는다.
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")  //{itemId}는 path variable
    public String updateItemFrom(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);         //이렇게 쓰는건 좋지않다. 예제를 위한 예제이다 .캐스팅 하는건 왠만하면 지양

        BookForm form = new BookForm();                 //라인 셀렉트를 이용하는걸 찾아보자
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")  //{itemId}는 path variable
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form){             //html에서 잡은 form이라는 attribute를 사용하기위해 @ModelAttribute사용

       /**
       * 준영속 엔티티?: 영속성 켄텍스가 더는 관리하지 않는 엔티티, 이미 DB에 한번 저장되어서 식별자가 존재하는것
       * 지금은 book이 준영속 엔티티다다.
       * 문제는_ JPA가 관리를 안한다. 왜냐면 new로 해서 만들었기때문에,
      */                                  ///alt+ shift+ insert를 하면 column select 모드가 됨
/*        Book book = new Book();           //shift+ tap 하면 옆으로 땡겨짐
        book.setId(form.getId());             //)ctrl+ shift+ u 하면 upper case 됨
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());*/          //이렇게 어설프게 엔티티를 설정 하지 말자.
                                                //여기서 id값을 건드는건 진짜 조심해야한다.

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
