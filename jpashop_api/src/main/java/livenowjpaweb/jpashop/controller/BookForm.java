package livenowjpaweb.jpashop.controller;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id;                     //alt+ shift+ insert를 하면 column select 모드가 됨
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

}
