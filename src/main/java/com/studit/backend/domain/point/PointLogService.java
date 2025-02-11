package com.studit.backend.domain.point;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PointLogService {//포인트 로그
    private final PointLogRepository pointLogRepository;

    public PointLog createPointLog(PointLogRequest pointLogRequest) {//임시 포인트 로그 생성
        return pointLogRepository.save(PointLogRequest.pointLogForm(pointLogRequest));}

    public PointLog getPointLog(Long pointLogId) {
        return pointLogRepository.findById(pointLogId).orElse(null);}

    public List<PointLog> getAll() {return pointLogRepository.findAll();}

    public List<PointLog> getEach(Boolean charge2, Boolean withdraw2,
                                  Boolean refund2, Boolean deduct2) {//기능에 해당되는 해당 포인트 로그들 조회
        //모든 로그들로 시작
        List<PointLog> pointLogs=pointLogRepository.findAll();

        //조건을 통과하는 해당 로그들
        //충전
        if(charge2!=null&&charge2){pointLogs=pointLogs.stream()
                .filter(log->log.getCharge()>0).collect(Collectors.toList());}

        //출금
        if(withdraw2!=null&&withdraw2){pointLogs=pointLogs.stream()
                .filter(log->log.getWithdraw()>0).collect(Collectors.toList());}

        //환불
        if(refund2!=null&&refund2){pointLogs=pointLogs.stream()
                .filter(log->log.getRefund()>0).collect(Collectors.toList());}

        //차감
        if(deduct2!=null&&deduct2){pointLogs=pointLogs.stream()
                .filter(log->log.getCharge()>0).collect(Collectors.toList());}
        return pointLogs;}

    public PointLog charge(Long pointLogId,PointLogRequest pointLogRequest) {//충전
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));
        long charge=pointLogRequest.getCharge();
        saved.setTotalPoint(saved.getTotalPoint()+charge);
        saved.charge(pointLogRequest);
        return pointLogRepository.save(saved);}

    public PointLog withdraw(Long pointLogId,PointLogRequest pointLogRequest) {//출금
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));
        long withdraw=pointLogRequest.getWithdraw();
        saved.setTotalPoint(saved.getTotalPoint()-withdraw);
        saved.withdraw(pointLogRequest);
        return pointLogRepository.save(saved);}

    public PointLog refund(Long pointLogId,PointLogRequest pointLogRequest) {//환불
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));
        long refund=pointLogRequest.getRefund();
        saved.setTotalPoint(saved.getTotalPoint()+refund);
        saved.refund(pointLogRequest);
        return pointLogRepository.save(saved);}

    public PointLog deduct(Long pointLogId,PointLogRequest pointLogRequest) {//차감
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));
        long deduct=pointLogRequest.getDeduct();
        saved.setTotalPoint(saved.getTotalPoint()-deduct);
        saved.deduct(pointLogRequest);
        return pointLogRepository.save(saved);}}