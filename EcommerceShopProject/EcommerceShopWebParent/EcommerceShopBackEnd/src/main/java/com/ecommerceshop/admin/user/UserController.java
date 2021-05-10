package com.ecommerceshop.admin.user;

import com.ecommerceshop.admin.errors.UserNotFoundException;
import com.ecommerceshop.common.entity.Role;
import com.ecommerceshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model){
        List<User> users = userService.getAllUsers();
        model.addAttribute("listUsers", users);
        return "users";
    }
    @GetMapping("/users/create")
    public String createNewUser(Model model){
        User user = new User();
        List<Role> roles = userService.getRoles();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveNewUser(User user, RedirectAttributes redirectAttributes) throws UserNotFoundException {

        boolean isUserUpdating = (user.getId() != null);

        if(isUserUpdating){
            User existingUser = userService.getById(user.getId());
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            } else {
                userService.encodePassword(user);
            }
        } else {
            userService.encodePassword(user);
        }

        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "User has been saved successfully");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        try {
            List<Role> roles = userService.getRoles();
            User user = userService.getById(id);

            model.addAttribute("roles", roles);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User");
            return "user_form";
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }

    }


    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){

        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "The user with ID: " +id + "has been deleted successfully");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";

    }



    }
