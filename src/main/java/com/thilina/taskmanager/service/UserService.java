package com.thilina.taskmanager.service;

import com.thilina.taskmanager.model.User;
import com.thilina.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existByemail(user.getEmail())) {
            throw new RuntimeException("Email already in use: " +  user.getEmail());
        }
        return userRepository.save(user);
    }
}
