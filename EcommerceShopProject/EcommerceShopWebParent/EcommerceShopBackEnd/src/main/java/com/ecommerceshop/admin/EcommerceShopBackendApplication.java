package com.ecommerceshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ecommerceshop.common.entity", "com.ecommerceshop.admin.user"})
public class EcommerceShopBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceShopBackendApplication.class, args);
    }

}
