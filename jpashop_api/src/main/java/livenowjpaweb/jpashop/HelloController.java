package livenowjpaweb.jpashop;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data","하하잇!");
        return "hello"; //이렇게 해도 Thymeleaf 가 자동적으로 template 의 hello.html를 찾아붐
                        //앞의 경로와 뒤의 확장자를 자동으로 설정.
    }
}
