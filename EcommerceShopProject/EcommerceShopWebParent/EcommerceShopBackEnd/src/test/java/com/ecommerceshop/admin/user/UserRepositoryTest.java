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
    public void createOneRoleUserTest(){
        Role role = testEntityManager.find(Role.class, 3L);
        User user = new User("user@gmail.com", "password", "Firstname", "LastName");
        user.addRole(role);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void createTwoRoleUserTest(){
        Role role1 = testEntityManager.find(Role.class, 3L);
        Role role2 = testEntityManager.find(Role.class, 4L);
        User user = new User("user2@gmail.com", "password2", "Firstname2", "LastName2");
        user.addRole(role1);
        user.addRole(role2);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void listAllUsersTest(){
        List<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void getUserByIdTest(){
        User user = userRepository.findById(1L).get();
        assertThat(user).isNotNull();
    }

    @Test
    public void updateUserDetailsTest(){
        User user = userRepository.findById(1L).get();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Test
    public void updateUserRolesTest(){
        User user = userRepository.findById(2l).get();
        Role role = testEntityManager.find(Role.class, 4L);
        Role newRole = testEntityManager.find(Role.class, 2L);
        user.getRoles().remove(role);
        user.addRole(newRole);
    }

    @Test
    public void deleteUserTest(){
        userRepository.deleteById(2L);
    }

    @Test
    public void getUserByEmailTest(){
        String email = "labajkoo@gmail.com";
        User user = userRepository.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void countByIdTest() {
        Long id = 1L;
        Long countById = userRepository.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void disableUserTest() {
        Long id = 1L;
        userRepository.updateEnableStatus(1L, false);

    }

    @Test
    public void enableUserTest() {
        Long id = 1L;
        userRepository.updateEnableStatus(1L, true);

    }


}
