package livenowjpaweb.jpashop.service;


import livenowjpaweb.jpashop.domain.Member;
import livenowjpaweb.jpashop.repository.MemberRepository;
import livenowjpaweb.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)      //데이터 변경은 트렌젝션이 필요함
@RequiredArgsConstructor            //선언된 모든 final필드가 포함된 생성자를 생성
public class MemberService {           //읽기전용으로 해놓으면 성능이 좋아짐, 이렇게 해놓으면 전반적으로 걸린다.
   // @Autowired  //스프링이 스프링 빈에 등록되어있는 멤버리포지토리를 인젝션됨
    private final MemberRepository memberRepository;    //변경할 일이 없기 때문
/*
    public MemberService(MemberRepository memberRepository) {   //Autowired를 안한 이유는 최신 스프링이 어노테이션이 없어도 인젝션을 해줌
        this.memberRepository = memberRepository;
    }*/

    //회원 가입
    @Transactional      //여기서는 readonly가 false로 (default가 false이기때문)
    public Long join(Member member){
        validateDuplicateMember(member);    //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    //회원 전체 조회
 //   @Transactional(readOnly = true)      //읽기전용으로 해놓으면 성능이 좋아짐
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //한명 조회
  //  @Transactional(readOnly = true)      //읽기전용으로 해놓으면 성능이 좋아짐
    public Member findOne(Long memberId){
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }
}
