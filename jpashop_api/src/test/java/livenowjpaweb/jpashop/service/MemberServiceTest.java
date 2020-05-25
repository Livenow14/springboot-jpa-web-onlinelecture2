package livenowjpaweb.jpashop.service;

import livenowjpaweb.jpashop.domain.Member;
import livenowjpaweb.jpashop.repository.MemberRepositoryOld;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)    //junit에 스프링이랑 같이 엮어서 실행함
@SpringBootTest                 // 스프링부트를 띄운상태로 테스트를 실행
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepository;
    @Autowired
    EntityManager em; //그래도 보고싶으면 이렇게 하면됨
    @Test

    public void  회원가입() throws Exception{       //insert를 안하는 이유는 rollback이 되기때문
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        //em.flush();//그래도 보고싶으면 이렇게 하면됨
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        memberService.join(member2); //에외가 발생한다.

/*        try {
            memberService.join(member2); //에외가 발생한다.
        }catch (IllegalStateException e ){
            return;
        }*/             //@Test(expected = IllegalStateException.class)를 하면  이 문구를 빼도됨

        //then
        fail("예외가 발생해야 한다. ");  //예외가 발생하지 않으면 테스트 창에 띄워줌
    }

}