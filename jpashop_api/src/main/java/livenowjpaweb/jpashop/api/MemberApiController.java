package livenowjpaweb.jpashop.api;

import livenowjpaweb.jpashop.domain.Member;
import livenowjpaweb.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

   /* @PostMapping("/api/v1/members") //RequestBody란, json으로 온 바디를 Member에 그대로 mapping해서 그대로 넣어줌
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){    //api를 만들때는 항상 이렇게 엔티티를 노출시키면 안된다. 절대로.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

    }*/

    /**
     * 등록 DTO라는 객체를 새로 만들었다.
     */
   @PostMapping("/api/v2/members")
   public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
       Member member = new Member();
       member.setName(request.getName());       //이렇게 하면 넘겨줄 수 있는걸 정해줄 수 있다.
       Long id = memberService.join(member);
       return new CreateMemberResponse(id);
   }

   @Data
   static class CreateMemberRequest{
       @NotEmpty
       private String name;
   }

    @Data   //응답값
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    /**
     * 수정
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName());        //이렇게 커맨드와 커리를 분리해야한다.
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }
    @Data
    @AllArgsConstructor
   static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
