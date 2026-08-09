package com.sekai.picture.control;

import com.sekai.picture.model.Result;
import com.sekai.picture.model.User;
import com.sekai.picture.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SekaiUserControl {

    @Resource
    private UserService userService;

    @PostMapping("/user/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("confirmPassword") String confirmPassword,
                           RedirectAttributes redirectAttributes) {
        if (user.getPassword() == null || !user.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "两次输入的密码不一致");
            return "redirect:/register";
        }
        Result<User> result = userService.register(user);
        if (!result.isSuccess()) {
            redirectAttributes.addFlashAttribute("error", result.getMessage());
            return "redirect:/register";
        }
        redirectAttributes.addFlashAttribute("message", "注册成功，请登录");
        return "redirect:/login";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        Result<User> result = userService.login(userName, password);
        if (!result.isSuccess()) {
            redirectAttributes.addFlashAttribute("error", result.getMessage());
            return "redirect:/login";
        }
        session.setAttribute("loginUser", result.getData());
        return "redirect:/home";
    }
}
