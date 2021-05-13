package com.ecommerceshop.admin.user;

import com.ecommerceshop.admin.errors.UserNotFoundException;
import com.ecommerceshop.admin.utils.FIleUploadUtil;
import com.ecommerceshop.common.entity.Role;
import com.ecommerceshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        return getAllUsers(1, "id", "asc", null, model);
    }

    @GetMapping("/users/page/{number}")
    public String getAllUsers(@PathVariable int number,
                              @Param("sortField") String sortField,
                              @Param("sortDirection") String sortDirection,
                              @Param("key") String key, Model model) {
        Page<User> page = userService.listUsersByPage(number, sortField, sortDirection, key);
        List<User> users = page.getContent();
        long totalElements = page.getTotalElements();
        long startCounter = (number - 1) * UserService.USERS_PER_PAGE + 1;
        long endCounter = startCounter + UserService.USERS_PER_PAGE - 1;
        if (endCounter > totalElements) {
            endCounter = totalElements;
        }
        model.addAttribute("currentPage", number);
        model.addAttribute("startCounter", startCounter);
        model.addAttribute("endCounter", endCounter);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", totalElements);

        model.addAttribute("listUsers", users);

        String reverseDirection = sortDirection.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseDirection", reverseDirection);

        model.addAttribute("key", key);
        return "users";
    }

    @GetMapping("/users/create")
    public String createNewUser(Model model) {
        User user = new User();
        List<Role> roles = userService.getRoles();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile file) throws UserNotFoundException, IOException {

        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            user.setPhoto(fileName);
            User savedUser = userService.saveUser(user);
            String directory = "user-photos/" + savedUser.getId();
            FIleUploadUtil.cleanDir(directory);
            FIleUploadUtil.saveFile(directory, fileName, file);

        } else {
            if (user.getPhoto().isEmpty()) {
                user.setPhoto(null);
            }
            userService.saveUser(user);
        }
        redirectAttributes.addFlashAttribute("message", "User has been saved successfully");
        return "redirect:/users/page/1?sortField=id&sortDirection=asc&key=" + user.getEmail();
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
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {

        try {
            String directory = "user-photos/" + id;
            FIleUploadUtil.cleanDir(directory);
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "The user with ID: " + id + "has been deleted successfully");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{enable}")
    public String updateStatus(@PathVariable Long id, @PathVariable boolean enable,
                               RedirectAttributes redirectAttributes, Model model) {
        userService.updateUserEnabledStatus(id, enable);
        if (enable == true)
            redirectAttributes.addFlashAttribute("message", "Update user ID" + id + " enable status to enabled");
        else
            redirectAttributes.addFlashAttribute("message", "Update user ID" + id + " enable status to disabled ");
        return "redirect:/users";

    }


}
