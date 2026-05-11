package com.example.testspot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 정적 파일(HTML, CSS, JS) 서빙 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /static/** 경로의 정적 파일 서빙
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        
        // 루트 경로(/)에서 index.html 서빙
        registry.addResourceHandler("/")
                .addResourceLocations("classpath:/static/index.html");
    }
}
