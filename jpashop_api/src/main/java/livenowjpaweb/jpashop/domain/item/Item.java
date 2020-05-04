package livenowjpaweb.jpashop.domain.item;


import livenowjpaweb.jpashop.domain.Category;
import livenowjpaweb.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   //상속관계 전략을 짜야하기 때문 single table 은 한 테이블에 다 때려박는것
@DiscriminatorColumn(name="dtype")  //저장을할 때 구분하기 위해
@Getter
@Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")         //다대다 예제를 위해, 실무에선 거의 안쓴다
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직 == //

    //stock 증가
    public void addStock(int quntity){
        this.stockQuantity+=quntity;
    }
    // item entity에 stockQuantity가 있으니 여기서 관리하는게 좋음
    //stock 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;

    }

}
