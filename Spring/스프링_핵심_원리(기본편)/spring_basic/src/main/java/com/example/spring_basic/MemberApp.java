package com.example.spring_basic;

import com.example.spring_basic.config.AppConfig;
import com.example.spring_basic.member.Grade;
import com.example.spring_basic.member.Member;
import com.example.spring_basic.member.MemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();

        // 스프링 컨테이너
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        Member member = new Member(1L, "김민지", Grade.VIP);
        memberService.join(member);

        Member member1 = memberService.findMember(1L);

        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + member1.getName());
    }
}
