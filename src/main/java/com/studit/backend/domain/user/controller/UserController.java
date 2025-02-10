package com.studit.backend.domain.user.controller;


import com.studit.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //유저 닉네임 수정
    @PostMapping
    public void UserNickName(@RequestBody String userNickName ) {
        userService.checkUserNickName(userNickName);
    }


}



