package com.studit.backend.domain.room.controller;

import com.studit.backend.domain.room.dto.StudyRoomResponse;
import com.studit.backend.domain.room.service.StudyRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class StudyRoomController {

    private final StudyRoomService studyRoomService;

    // 스터디룸 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllRooms(@RequestParam(defaultValue = "0") int page) {
        // 한 페이지당 5개
        int pageSize = 5;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<StudyRoomResponse.Summary> rooms = studyRoomService.getAllRooms(pageable);

        return ResponseEntity.ok(rooms);
    }
}
