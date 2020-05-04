package livenowjpaweb.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")        //default는 class 이름으로 들어감
@Getter
@Setter
public class Book extends Item {

    private String author;
    private String isbn;
}
