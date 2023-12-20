package com.webtut.dbwork.controllers;

import com.webtut.dbwork.domain.dto.UserDto;
import com.webtut.dbwork.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public List<UserDto> listUsers() {
        return userService.findAll();
    }

    @GetMapping(path = "/users/{user_id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("user_id") Long userId) {
        Optional<UserDto> foundUser = userService.findById(userId);
        return foundUser.map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/users/{user_id}")
    public ResponseEntity<UserDto> fullUpdateUser(
            @PathVariable("user_id") Long userId, @RequestBody UserDto userDto) {
        if (!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDto.setUserId(userId);
        UserDto savedUserDto = userService.save(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.OK);
    }

    @PatchMapping(path = "/users/{user_id}")
    public ResponseEntity<UserDto> partialUpdateUser(
            @PathVariable("user_id") Long userId,
            @RequestBody UserDto userDto) {
        if (!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDto.setUserId(userId);
        UserDto savedUserDto = userService.partialUpdate(userId, userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/users/{user_id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("user_id") Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}