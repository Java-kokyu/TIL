package com.example.spring_basic.beanfind;

import com.example.spring_basic.config.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefiniationNames = ac.getBeanDefinitionNames();
        // iter
        for (String beanDefiniationName : beanDefiniationNames) {
            Object bean = ac.getBean(beanDefiniationName);
            System.out.println("beanDefiniationName = " + beanDefiniationName);
            System.out.println("bean = " + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefiniationNames = ac.getBeanDefinitionNames();
        // iter
        for (String beanDefiniationName : beanDefiniationNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefiniationName);
            // ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
            // ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈
            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefiniationName);
                System.out.println("beanDefiniationName = " + beanDefiniationName);
                System.out.println("bean = " + bean);
            }
        }
    }
}
