package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.entity.User;
import com.pavan.ai_resume_reviewer.model.UserRequest;
import com.pavan.ai_resume_reviewer.model.UserResponse;
import com.pavan.ai_resume_reviewer.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}