package com.ecommerceshop.admin.user;

import com.ecommerceshop.admin.config.security.ShopUserDetails;
import com.ecommerceshop.admin.errors.UserNotFoundException;
import com.ecommerceshop.admin.utils.FIleUploadUtil;
import com.ecommerceshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    private UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShopUserDetails loggedUser,
                              Model model) {
        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);
        model.addAttribute("user", user);
        return "account_form";
    }

    @PostMapping("/account/update")
    public String saveDetailsAccount(User user, RedirectAttributes redirectAttributes,
                                     @RequestParam("image") MultipartFile file,
                                     @AuthenticationPrincipal ShopUserDetails loggedUser) throws UserNotFoundException, IOException {

        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            user.setPhoto(fileName);
            User savedUser = userService.updateAccount(user);
            String directory = "user-photos/" + savedUser.getId();
            FIleUploadUtil.cleanDir(directory);
            FIleUploadUtil.saveFile(directory, fileName, file);

        } else {
            if (user.getPhoto().isEmpty()) {
                user.setPhoto(null);
            }
            userService.updateAccount(user);
        }
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "Account details have been updated.");
        return "redirect:/account";
    }
}
