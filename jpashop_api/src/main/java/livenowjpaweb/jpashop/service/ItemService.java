package livenowjpaweb.jpashop.service;

import livenowjpaweb.jpashop.domain.item.Book;
import livenowjpaweb.jpashop.domain.item.Item;
import livenowjpaweb.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // readonly가 true이기때문
    public void saveItem(Item item){
        itemRepository.save(item);
    }


    /**
     * 준영속 엔티티를 수정하는 2가지 방법중 변경 감지 기능
     * findItem으로 찾아온 애는 영속상태이다. 트랜젝션에 의해서 커밋이된다.
     * 이때 jpa는 플러쉬 라는걸 날림. 이는 영속성 컨텍스트 중에 변경된게 먼지 찾는것.
     * 변경 감지 기능은 원하는 속성만 선택해서 사용할 수 잇음.
     * 이렇게 단발성으로 업데이트를 하지않는다. 의미있는 메서드를 해야한다.
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    /**
     * 준영속 엔티티를 수정하는 2가지 방법중 병합(merger)기능
     * 하지만 이것은 많이 사용하지 않는다.
     * 변경감지 기능은 원하는 속성만 선택해서 사용할 수 잇지만.
     * 병합은 모든 속성이 변경된다. 그렇기 때문에 병합시 값이 없으면 null이 된다.
     */
/*    @Transactional
    public Item updateItem(Long itemId, Book param){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        return findItem;
    }*/
    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }



}
