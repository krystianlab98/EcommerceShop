package com.ecommerceshop.admin.user;


import com.ecommerceshop.admin.errors.UserNotFoundException;
import com.ecommerceshop.common.entity.Role;
import com.ecommerceshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User saveUser(User user){
        encodePassword(user);
        return userRepository.save(user);
    }
    public List<Role> getRoles(){
        return roleRepository.findAll();
    }
    public void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
    public boolean isEmailUnique(Long id, String email){
        User user = userRepository.getUserByEmail(email);
        if(user == null) return true;
        boolean isNewUser = ( id == null);
        if(isNewUser){
            if(user != null) return false;
        } else {
            if(user.getId() != id){
                return false;
            }
        }
        return true;
    }

    public User getById(Long id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find any user with ID: " + id);
        }
    }

    public void deleteById(Long id) throws UserNotFoundException {
        Long count = userRepository.countById(id);

        if (count == null || count == 0) {
            throw new UserNotFoundException("Could not find any user with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Long id, boolean enable) {
        userRepository.updateEnableStatus(id, enable);
    }

}
