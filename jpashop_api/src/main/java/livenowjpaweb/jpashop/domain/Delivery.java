package livenowjpaweb.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)   //ordinal은 순자로 들어감 123, 중간에 다른 상태가 생가면 망함 그래서 String으로 함
    private DeliveryStatus status; //READY, COMP

}
