package com.example.spring_basic.member;

import com.example.spring_basic.config.AppConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @Test
    void join() {
        //given
        Member member = new Member(1L, "김민지", Grade.VIP);

        //when
        memberService.join(member);
        Member findMember =  memberService.findMember(1L);

        //then
        Assertions.assertEquals(member, findMember);
    }
}
