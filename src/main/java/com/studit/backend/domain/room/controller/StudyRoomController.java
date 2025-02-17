package com.studit.backend.domain.room.controller;

import com.studit.backend.domain.room.service.StudyRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class StudyRoomController {

    private final StudyRoomService studyRoomService;


}
