package livenowjpaweb.jpashop.repository;

import livenowjpaweb.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){   //id 값이 없다는거는 새로 생성하는거임
            em.persist(item);       //신규로 등록
        } else {
            em.merge(item);         //update라고 생각하면됨, 하지만 진짜 update는 아님
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
