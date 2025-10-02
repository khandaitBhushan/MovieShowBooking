package com.org.service;

import com.org.dto.UserDto;
import com.org.exception.ResourceNotFoundException;
import com.org.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.org.Models.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private UserDto createUser(UserDto userDto){
        User user = mapToUser(userDto);
        return mapToDto(userRepository.save(user));
    }

    private UserDto getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User does not exist...."));
        return mapToDto(user);
    }


    public List<UserDto> getAllUser(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    public User mapToUser(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    public UserDto mapToDto(User user){
        UserDto userDto=new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }
}
