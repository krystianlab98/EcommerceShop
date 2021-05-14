package com.ecommerceshop.admin.user;


import com.ecommerceshop.admin.errors.UserNotFoundException;
import com.ecommerceshop.common.entity.Role;
import com.ecommerceshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public static final int USERS_PER_PAGE = 4;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> listUsersByPage(int pageNumber, String sortField, String sortDirection, String key) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);

        if (key != null) {
            return userRepository.findAll(key, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public User saveUser(User user) {
        boolean isUserUpdating = (user.getId() != null);

        if (isUserUpdating) {
            User existingUser = userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        return userRepository.save(user);
    }

    public User updateAccount(User updatedUser) {
        User dbUser = userRepository.findById(updatedUser.getId()).get();
        if (!updatedUser.getPassword().isEmpty()) {
            dbUser.setPassword(updatedUser.getPassword());
            encodePassword(dbUser);
        }
        if (updatedUser.getPhoto() != null) {
            dbUser.setPhoto(updatedUser.getPhoto());
        }
        dbUser.setFirstName(updatedUser.getFirstName());
        dbUser.setLastName(updatedUser.getLastName());

        return userRepository.save(dbUser);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Long id, String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) return true;
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
