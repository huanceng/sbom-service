package org.openeuler.sbom.manager;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 运行本地模式的跨域请求
 */
@Configuration
public class CrossConfig implements WebMvcConfigurer {
    static final String[] ORIGINS = new String[]{"GET", "POST", "PUT", "DELETE"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080/","http://127.0.0.1:8080/").allowCredentials(true).allowedMethods(ORIGINS).maxAge(3600);
    }
}