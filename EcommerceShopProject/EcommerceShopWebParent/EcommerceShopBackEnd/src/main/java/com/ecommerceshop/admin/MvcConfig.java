package com.ecommerceshop.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path photoDirectory = Paths.get("user-photos");

        String photoPath = photoDirectory.toFile().getAbsolutePath();

        registry.addResourceHandler("/user-photos/**")
                .addResourceLocations("file:/" + photoPath + "/");
    }
}
