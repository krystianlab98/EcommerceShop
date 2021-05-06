package com.ecommerceshop.admin.user;

import com.ecommerceshop.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void createAdminRole(){
        Role roleAdmin = new Role("Admin", "Able to manage everything" );
        Role savedRole = roleRepository.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }
    @Test
    public void createOtherRole(){
        Role editor = new Role("Editor", "Able to manage categories, brands," +
                "products, articles and menu" );
        Role shipper = new Role("Shipper", "Able to view products, categories, orders " +
                "and update orders status");
        Role salesperson = new Role("Salesperson", "Able to manage price," +
                "customers, shipping, orders and sales report" );
        Role assistant = new Role("Assistant", "Able to manage questions and reviews");

        roleRepository.saveAll(List.of(editor, shipper,salesperson, assistant));

    }

}
