/*
package livenowjpaweb.jpashop;

import livenowjpaweb.jpashop.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional  //이것이 테스트 케이스에 있으면, 테스트가 끝난다음에 rollback  한다. 이것은 transaction 속성
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);
        //then

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        System.out.println("findMember == member :  " + (findMember == member));    //영속성 컨텍스트에 의해 원래 있던거라 조회한것이랑 같다고 인식

    }


}*/
