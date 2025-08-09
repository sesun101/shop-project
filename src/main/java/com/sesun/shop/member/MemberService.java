package com.sesun.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public void saveMember(String username, String password, String displayName) throws Exception {
        Member member = new Member();

        if (memberRepository.existsByUsername(username)) {
            throw new Exception("존재하는 아이디");
        }

        if (username.length() < 4 || password.length() < 8) {
            throw new Exception("너무 짧음");
        }

        if (ReservedUsernameChecker.isReserved(username)) {
            throw new Exception("금지된 아이디 사용");
        }

        String hashedPassword = passwordEncoder.encode(password);
        member.setUsername(username);
        member.setPassword(hashedPassword);
        member.setDisplayName(displayName);
        memberRepository.save(member);
    }
}

class ReservedUsernameChecker {
    private static final Set<String> RESERVED_USERNAME = Set.of(
            "guest",
            "admin",
            "root",
            "system",
            "null",
            "undefined",
            "superuser"
    );

    public static boolean isReserved(String username) {
        return username != null && RESERVED_USERNAME.contains(username.toLowerCase());
    }
}
