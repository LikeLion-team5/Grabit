package com.ll.grabit.boundedcontext.member.service;


import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.dto.MemberEditDto;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional()
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findByNickname(String nickname) { return  memberRepository.findByNickname(nickname); }

    @Transactional
    public RsData<Member> join(MemberCreateDto memberCreateDto) {

        String username = memberCreateDto.getUsername();
        String password = memberCreateDto.getPassword();
        String confirmPassword = memberCreateDto.getConfirmPassword();
        String email = memberCreateDto.getEmail();
        String nickname = memberCreateDto.getNickname();
        String phone = memberCreateDto.getPhone();


        if (!password.equals(confirmPassword)) {
            return RsData.of("F-2", "비밀번호와 비밀번호 확인이  일치하지 않습니다.");
        }

        if (findByUsername(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        if (findByUsername(nickname).isPresent()) {
            return RsData.of("F-3", "해당 닉네임(%s)은 이미 사용중입니다.".formatted(nickname));
        }


        return createAndSave(username, password, email , nickname, phone, "NORMAL");


    }

    private RsData<Member> createAndSave(String username, String password, String email, String nickname, String phone, String providerTypeCode) {
        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .providerTypeCode(providerTypeCode)
                .email(email)
                .phone(phone)
                .nickname(nickname)
                .build();

        Member savedMember = memberRepository.save(member);
        return RsData.of("S-1", "회원가입이 완료되었습니다.", savedMember);
    }

    @Transactional
    public RsData<Member> whenSocialLogin(String providerTypeCode, String username) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return RsData.of("S-1", "로그인 되었습니다.", opMember.get());

        return createAndSave(username, "", "", "", "", providerTypeCode);

    }

    @Transactional
    public RsData<Member> edit(MemberEditDto memberEditDto, Long memberId) {

        String nickname = memberEditDto.getNickname();
        String email = memberEditDto.getEmail();
        String phone = memberEditDto.getPhone();

        Optional<Member> opMember = findById(memberId);

        if (opMember.isEmpty()) {
            return RsData.of("F-1", "존재하지않는 회원입니다.");
        }

        Member member = opMember.get();

        if (!member.getNickname().equals(nickname) && findByNickname(nickname).isPresent()) {
            return RsData.of("F-3", "해당 닉네임(%s)은 이미 사용중입니다.".formatted(nickname));
        }

        member.updateProfile(nickname, email, phone);

        return RsData.of("S-2", "내 정보 수정이 완료되었습니다.", member);
    }
}

