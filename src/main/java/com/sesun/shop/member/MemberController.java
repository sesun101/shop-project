package com.sesun.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    @GetMapping("/register")
    String showRegister(Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/list";
        }
        return "register.html";
    }
    @PostMapping("/register")
    String register(String displayName, String username, String password) {
        try {
            memberService.saveMember(username, password, displayName);
            return "redirect:/login";
        } catch(Exception e) {
            e.printStackTrace();
            return "redirect:/register?error=short";
        }
    }
    @GetMapping("/login")
    String login() {
        return "login.html";
    }
    @GetMapping("/my-page")
    String mypage(Authentication auth) {
        System.out.println(auth.getAuthorities().contains(
                new SimpleGrantedAuthority("user")
        ));
        return "mypage.html";
    }
    @PostMapping("/logout")
    String logout() {
        return "redirect:/list";
    }
    @GetMapping("/user/1")
    @ResponseBody
    MemberDto getUser() {
        Optional<Member> a = memberRepository.findById(1L);
        Member result = a.get();
        MemberDto data = new MemberDto(result.getUsername(), result.getDisplayName());
        return data;
    }
}

class MemberDto {
    public String username;
    public String displayName;

    MemberDto(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }
}