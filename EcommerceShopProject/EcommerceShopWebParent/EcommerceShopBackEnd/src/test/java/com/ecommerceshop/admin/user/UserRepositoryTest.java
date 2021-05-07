package com.ecommerceshop.admin.user;

import com.ecommerceshop.common.entity.Role;
import com.ecommerceshop.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void createOneRoleUser(){
        Role role = testEntityManager.find(Role.class, 3L);
        User user = new User("user@gmail.com", "password", "Firstname", "LastName");
        user.addRole(role);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void createTwoRoleUser(){
        Role role1 = testEntityManager.find(Role.class, 3L);
        Role role2 = testEntityManager.find(Role.class, 4L);
        User user = new User("user2@gmail.com", "password2", "Firstname2", "LastName2");
        user.addRole(role1);
        user.addRole(role2);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void listAllUsers(){
        List<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void getUserById(){
        User user = userRepository.findById(1L).get();
        assertThat(user).isNotNull();
    }

    @Test
    public void updateUserDetails(){
        User user = userRepository.findById(1L).get();
        user.setEnabled(true);
        userRepository.save(user);
    }




}
