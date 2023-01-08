package com.example.spring_basic;

import com.example.spring_basic.config.AppConfig;
import com.example.spring_basic.member.Grade;
import com.example.spring_basic.member.Member;
import com.example.spring_basic.member.MemberService;
import com.example.spring_basic.order.Order;
import com.example.spring_basic.order.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);

        Long memberId = 1L;
        Member member = new Member(memberId, "김민지", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "붕어빵", 2000);

        System.out.println("order = " + order);
    }
}
