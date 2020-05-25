package livenowjpaweb.jpashop.repository;

import livenowjpaweb.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * findBy하고 NAme이 있으면
     * select m from Member m where m.name = :name 을 만들어줌
     * 하지만 스프링 데이터 JPA는 JPA를 사용해서 이런 기능을 제공한 뿐이다.
     * 결국 JPA 자체를 잘 이해하는 것이 가장 중요하다.
     * */
    List<Member> findByName(String name);
}
