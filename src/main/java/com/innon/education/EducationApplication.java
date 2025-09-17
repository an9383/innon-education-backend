package com.innon.education;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class EducationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducationApplication.class, args);

    }
    @Value("${cors.server.url}")
    String serverUrl;
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**").allowedOrigins("http://localhost", "http://127.0.0.1:5500","http://localhost:8080","http://10.200.1.13",
                        "http://218.238.95.78","http://218.238.95.78/","http://218.238.95.79","http://218.238.95.79/","http://10.224.100.59",
                        "https://exdip.inno-n.com", "https://dip.inno-n.com", "https://exdip.inno-n.com/", "https://dip.inno-n.com/",
                        "http://10.78.114.2:3333/", "http://10.78.112.113:3333/", "http://10.78.113.89:3333/",
                        "http://localhost:3333/", "http://localhost:3333","http://10.224.84.99:3333",
                        "https://dev.vgmp.co.kr","http://www.vgmp.co.kr") .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name());;
                // "http://10.224.110.52:8080", "http://10.224.110.52:8080/" 임시추가

            }
        };
    }
}
