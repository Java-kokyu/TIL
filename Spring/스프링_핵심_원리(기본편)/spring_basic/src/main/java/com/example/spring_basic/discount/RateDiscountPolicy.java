package com.example.spring_basic.discount;

import com.example.spring_basic.member.Grade;
import com.example.spring_basic.member.Member;

public class RateDiscountPolicy implements DiscountPolicy{
    private final int DISCOUNT_RATE = 10;
    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP) {
            return (int) (price * (DISCOUNT_RATE * 0.01));
        }
        return 0;
    }
}
