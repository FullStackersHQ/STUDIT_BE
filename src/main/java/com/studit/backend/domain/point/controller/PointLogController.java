package com.studit.backend.domain.point.controller;
import com.studit.backend.domain.point.dto.PointLogRequest;
import com.studit.backend.domain.point.service.PointLogService;
import com.studit.backend.domain.point.PointLogType;
import com.studit.backend.domain.point.entity.PointLog;
import com.studit.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.LogSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pointLog")
@RequiredArgsConstructor
public class PointLogController {//포인트 로그
    private final PointLogService pointLogService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping//임시 포인트 로그 생성
    public ResponseEntity<?> createPointLog
            (@RequestHeader("Authroization") String token,
             @Validated @RequestBody PointLogRequest pointLogRequest) {

        //JWT 에서 userId 추출
        Long userId=jwtTokenProvider.getUserIdFromToken(token);
        pointLogRequest.setUserId(userId);

        PointLog pointLog = pointLogService.createPointLog(pointLogRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @GetMapping("/get/{pointLogId}")//포인트 로그 조회
    public ResponseEntity<PointLog>getPointLog(@PathVariable Long pointLogId){
        PointLog pointLog=pointLogService.getPointLog(pointLogId);
        return ResponseEntity.ok(pointLog);}

    @GetMapping//한 번에 여러 개 로그 조회(기본 10개씩)
    public ResponseEntity<List<PointLog>> getPointLogs(@RequestParam Long userId,
                                                       @RequestParam(required = false, defaultValue = "0") Long cursor,
                                                       @RequestParam(defaultValue = "10") int pageSize){
        List<PointLog> pointLogs=pointLogService.getPointLogs
                (userId,cursor,pageSize);return ResponseEntity.ok(pointLogs);}

    @GetMapping("/getAll") public List<PointLog> getAll(){//모든 포인트 로그 조회
        List<PointLog> pointLog=pointLogService.getAll();
        pointLog.forEach(System.out::println);return pointLog;}

    @GetMapping("/getEach") public List<PointLog> getEach(@RequestParam
                                                                  (required = false) PointLogType pointLogType){//기능에 해당되는 해당 포인트 로그들 조회
        List<PointLog> pointLogs=pointLogService.getEach(pointLogType);
        pointLogs.forEach(System.out::println);return pointLogs;}

    @PutMapping("/charge/{pointLogId}")//충전
    public ResponseEntity<?> charge(@PathVariable Long pointLogId,
    @RequestHeader("Authorization") String token,
    @Validated @RequestBody PointLogRequest pointLogRequest) {
        Long userId=jwtTokenProvider.getUserIdFromToken(token);
        PointLog pointLog = pointLogService.charge(pointLogId, pointLogRequest, userId);

        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()+pointLogRequest.getChangePoint());
        pointLogRequest.setTotalWithdrawPoint(pointLogRequest
                .getTotalWithdrawPoint()+pointLogRequest.getChangePoint());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @PutMapping("/withdraw/{pointLogId}")//출금
    public ResponseEntity<?> withdraw(@PathVariable Long pointLogId,
                                      @RequestHeader("Authorization") String token,
                                      @Validated @RequestBody PointLogRequest pointLogRequest) {
        Long userId=jwtTokenProvider.getUserIdFromToken(token);
        PointLog pointLog = pointLogService.withdraw(pointLogId, pointLogRequest,userId);
        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()-pointLogRequest.getChangePoint());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @PutMapping("/refund/{pointLogId}")//환불
    public ResponseEntity<?> refund(@PathVariable Long pointLogId,
                                    @RequestHeader("Authorization") String token,
                                    @Validated @RequestBody PointLogRequest pointLogRequest) {
        Long userId=jwtTokenProvider.getUserIdFromToken(token);
        PointLog pointLog = pointLogService.refund(pointLogId, pointLogRequest, userId);
        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()+pointLogRequest.getChangePoint());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @PutMapping("/deduct/{pointLogId}")//차감
    public ResponseEntity<?> deduct(@PathVariable Long pointLogId,
                                    @RequestHeader("Authorization") String token,
                                    @Validated @RequestBody PointLogRequest pointLogRequest) {
        Long userId=jwtTokenProvider.getUserIdFromToken(token);
        PointLog pointLog = pointLogService.deduct(pointLogId, pointLogRequest,userId);

        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()-pointLogRequest.getChangePoint());
        pointLogRequest.setTotalDeductPoint(pointLogRequest
                .getTotalDeductPoint()+pointLogRequest.getChangePoint());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @PutMapping("/reward/{pointLogId}")//보상
    public ResponseEntity<?> reward(@PathVariable Long pointLogId,
                                    @RequestHeader("Authorization") String token,
                                    @Validated @RequestBody PointLogRequest pointLogRequest) {
        Long userId=jwtTokenProvider.getUserIdFromToken(token);
        PointLog pointLog = pointLogService.reward(pointLogId, pointLogRequest,userId);

        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()+pointLogRequest.getChangePoint());
        pointLogRequest.setTotalRewardPoint(pointLogRequest
                .getTotalRewardPoint()+pointLogRequest.getChangePoint());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}}