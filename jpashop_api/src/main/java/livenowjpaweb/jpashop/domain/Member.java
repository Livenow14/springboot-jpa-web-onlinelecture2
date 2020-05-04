package livenowjpaweb.jpashop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
        @Id @GeneratedValue
        @Column(name = "member_id")
        private Long id;


        private String name;

        @Embedded
        private Address address;

        @OneToMany(mappedBy = "member") //거울일 뿐이야 락 ㅗ하는것
        private List<Order> orders = new ArrayList<>();

}