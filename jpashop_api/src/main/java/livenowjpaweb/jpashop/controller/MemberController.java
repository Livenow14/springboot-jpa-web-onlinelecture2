package livenowjpaweb.jpashop.controller;


import livenowjpaweb.jpashop.domain.Address;
import livenowjpaweb.jpashop.domain.Member;
import livenowjpaweb.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){  //Model이란? 컨트롤러에서 뷰르 넘어갈때 데이터를 실어서 넘김 memberFrom이라는 껍데기를 넘김
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";

    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){   //valid를 통해 memberform에 있는 validation을 사용하게 해줌
                                                                          //BindingResult는 오류를 담고 밑에걸 실행함, 여기서 entity를 바로 쓰면 안된다.

        if(result.hasErrors()){             //다시 밑에 페이지로 가게 도와줌, valid에서 저장된걸
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";        //이러면 첫번째 페이지로 넘어감
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();         // API를 만들시에는 절대로 entity를 외부에 반환하면 안된다.
        model.addAttribute("members", members);        // 템플릿 엔진에서는 어느정도 괜찮다.

        return "members/memberList";
    }


}
