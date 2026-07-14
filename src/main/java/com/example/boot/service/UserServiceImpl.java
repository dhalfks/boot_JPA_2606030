package com.example.boot.service;

import com.example.boot.dto.UserDTO;
import com.example.boot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public String insert(UserDTO userDTO) {
        return "";
    }
}
