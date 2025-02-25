package com.studit.backend.domain.room.controller;

import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.room.dto.StudyRoomRequest;
import com.studit.backend.domain.room.dto.StudyRoomResponse;
import com.studit.backend.domain.room.service.StudyRoomService;
import com.studit.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class StudyRoomController {

    private final StudyRoomService studyRoomService;
    private final JwtTokenProvider jwtTokenProvider;

    // 스터디룸 목록 조회
    @GetMapping
    public ResponseEntity<Page<StudyRoomResponse.Summary>> getAllRooms(@RequestParam(defaultValue = "0") int page) {
        // 한 페이지당 5개
        int pageSize = 5;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<StudyRoomResponse.Summary> rooms = studyRoomService.getAllRooms(pageable);

        return ResponseEntity.ok(rooms);
    }

    // 스터디룸 목록 조회 (검색, 필터링 적용)
    @GetMapping("/search")
    public ResponseEntity<Page<StudyRoomResponse.Summary>> getSearchRooms(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) StudyCategory category,
            @RequestParam(required = false) Integer minDeposit,
            @RequestParam(required = false) Integer maxDeposit,
            @RequestParam(required = false) Integer minGoalTime,
            @RequestParam(required = false) Integer maxGoalTime,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 한 페이지당 5개
        int pageSize = 5;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<StudyRoomResponse.Summary> searchRooms = studyRoomService.getSearchRooms(
                title, category, minDeposit, maxDeposit, minGoalTime, maxGoalTime, pageable);

        return ResponseEntity.ok(searchRooms);
    }

    // 스터디룸 상세 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<StudyRoomResponse.Detail> getDetailRoom(@PathVariable Long roomId) {
        StudyRoomResponse.Detail studyRoom = studyRoomService.getDetailRoom(roomId);

        return ResponseEntity.ok(studyRoom);
    }

    // 스터디룸 수정 (제목, 설명, 태그 수정 가능)
    @PutMapping("/{roomId}")
    public ResponseEntity<String> updateRoom(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token,
            @Validated @RequestBody StudyRoomRequest.Update request) {
        // JWT 에서 userId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        studyRoomService.updateRoom(roomId, request, userId);

        return ResponseEntity.ok("스터디룸이 수정되었습니다.");
    }

    // 스터디룸 삭제
    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token) {
        // JWT 에서 userId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        studyRoomService.deleteRoom(roomId, userId);

        return ResponseEntity.ok("스터디룸이 삭제되었습니다.");
    }

    // 스터디룸 나가기 (Only 스터디원)
    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<String> leaveRoom(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        studyRoomService.leaveRoom(roomId, userId);
        return ResponseEntity.ok("스터디룸을 나갔습니다.");
    }

    // 공지사항 작성 또는 수정 (Only 스터디장)
    @PostMapping("/{roomId}/notices")
    public ResponseEntity<String> createOrUpdateNotice(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token,
            @RequestBody StudyRoomRequest.NoticeContent request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        studyRoomService.createOrUpdateNotice(roomId, userId, request.getContent());
        return ResponseEntity.ok("공지사항이 저장되었습니다.");
    }

    // 공지사항 삭제 (Only 스터디장)
    @DeleteMapping("/{roomId}/notices")
    public ResponseEntity<String> deleteNotice(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        studyRoomService.deleteNotice(roomId, userId);
        return ResponseEntity.ok("공지사항이 삭제되었습니다.");
    }
}
