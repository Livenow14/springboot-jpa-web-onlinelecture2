package livenowjpaweb.jpashop.repository;


import livenowjpaweb.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository     //스프링 빈으로 등록 component 대상이 되기때문에
@RequiredArgsConstructor    //이게 있으니 persistanceContext가 필요없어짐, 선언된 모든 final필드가 포함된 생성자를 생성
public class MemberRepository {

  //  @PersistenceContext     //이게 있으면 jpa entitymanager를 주입해줌

    private final EntityManager em;



    public void save (Member member){
        em.persist(member);             //영속성 컨텍스트에 member객체를 넣음 transaction이 커밋되는 시점에 디비에 반영됨
    }

    public Member findOne(Long id ){
            return em.find(Member.class, id);       //jpa의 메서드를 사용, 단건조회기 때문에, 타임 , pk를 넣음
    }

    public List<Member> findAll(){
/*        List<Member> result = em.createQuery("select m from Member m ", Member.class)
            .getResultList();

        return result;*/        //이렇게 쿼리를 만들어 줘야하고, result를 클릭후 ctrl + alt + n을 누르면 줄여줌
                                // sql는 테이블 대상으로 쿼리하면 jpql는 entity 객체를 통해 함

        return em.createQuery("select m from Member m ", Member.class)
            .getResultList();
}
    public List<Member> findByName(String name){            // 이름에 의해서 조회하는 기능, 파라미터 바인딩
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
