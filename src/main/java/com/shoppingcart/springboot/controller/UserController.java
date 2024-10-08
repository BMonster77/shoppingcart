package com.shoppingcart.springboot.controller;

import com.shoppingcart.springboot.service.UserService;
import com.shoppingcart.springboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping("/user/login")
    public String login(@RequestBody User user) {
        return null;
    }


    @PostMapping("/user/logout")
    public String logout() {
        // 注销当前用户的逻辑
        return "Success";
    }


    @PostMapping("/user/change-password")
    public String changePassword(@RequestParam Long userId,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword) {

        return "Password changed successfully!";
    }


    @PostMapping("/user/request-password-reset")
    public String requestPasswordReset(@RequestParam String emailOrUsername) {

        return "Password reset link or code sent to your email!";
    }



    @GetMapping("/user/{userId}")
    public User getUserInfo(@PathVariable Long userId) {

        return new User(); // 返回用户基本信息
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        return null;
    }


}
