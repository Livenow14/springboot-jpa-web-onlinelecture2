package livenowjpaweb.jpashop.domain;


import livenowjpaweb.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany     //보여주기 위해서 만든것
    @JoinTable(name = "category_item",
        joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name="item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)  //Manythoone은 default가 Eager이기 때문에 다바꿔야함
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //=연관관계 메서드 ==//양방향일때 연관관계를 한번에 설정
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

}
