package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.model.UserRequest;
import com.pavan.ai_resume_reviewer.model.UserResponse;
import com.pavan.ai_resume_reviewer.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }
}