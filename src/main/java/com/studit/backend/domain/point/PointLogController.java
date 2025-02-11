package com.studit.backend.domain.point;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pointLog")
@RequiredArgsConstructor
public class PointLogController {//포인트 로그
    private final PointLogService pointLogService;

    @PostMapping("/create")//임시 포인트 로그 생성
    public ResponseEntity<?> createPointLog(@RequestBody PointLogRequest pointLogRequest) {
        PointLog pointLog = pointLogService.createPointLog(pointLogRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @GetMapping("/get/{pointLogId}")//포인트 로그 2조회
    public ResponseEntity<PointLog>getPointLog(@PathVariable Long pointLogId){
        PointLog pointLog=pointLogService.getPointLog(pointLogId);
        return ResponseEntity.ok(pointLog);}

    @GetMapping("/getAll") public List<PointLog> getAll(){//모든 포인트 로그 조회
        List<PointLog> pointLog=pointLogService.getAll();
        pointLog.forEach(System.out::println);return pointLog;}

    @GetMapping("/getEach") public List<PointLog> getEach(@RequestParam(required = false) Boolean charge2,
    @RequestParam(required = false) Boolean withdraw2, @RequestParam(required = false) Boolean refund2,
    @RequestParam(required = false) Boolean deduct2){//기능에 해당되는 해당 포인트 로그들 조회

        List<PointLog> pointLogs=pointLogService.getEach(charge2,withdraw2,refund2,deduct2);
        pointLogs.forEach(System.out::println);return pointLogs;}

    @PutMapping("/charge/{pointLogId}")//충전
    public ResponseEntity<?> charge(@PathVariable Long pointLogId,
                                    @RequestBody PointLogRequest pointLogRequest) {
        PointLog pointLog = pointLogService.charge(pointLogId, pointLogRequest);
        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()+pointLogRequest.getWithdraw());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @PutMapping("/withdraw/{pointLogId}")//출금
    public ResponseEntity<?> withdraw(@PathVariable Long pointLogId,
                                      @RequestBody PointLogRequest pointLogRequest) {
        PointLog pointLog = pointLogService.withdraw(pointLogId, pointLogRequest);
        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()-pointLogRequest.getWithdraw());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @PutMapping("/refund/{pointLogId}")//환불
    public ResponseEntity<?> refund(@PathVariable Long pointLogId,
                                    @RequestBody PointLogRequest pointLogRequest) {
        PointLog pointLog = pointLogService.refund(pointLogId, pointLogRequest);
        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()+pointLogRequest.getRefund());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}

    @PutMapping("/deduct/{pointLogId}")//차감
    public ResponseEntity<?> deduct(@PathVariable Long pointLogId,
                                    @RequestBody PointLogRequest pointLogRequest) {
        PointLog pointLog = pointLogService.deduct(pointLogId, pointLogRequest);
        pointLogRequest.setTotalPoint(pointLogRequest.getTotalPoint()-pointLogRequest.getDeduct());
        return ResponseEntity.status(HttpStatus.CREATED).body(pointLog);}}