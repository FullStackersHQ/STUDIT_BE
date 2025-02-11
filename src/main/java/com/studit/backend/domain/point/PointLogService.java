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

    public List<PointLog> getEach(Boolean charge, Boolean withdraw,
                                  Boolean refund, Boolean deduct) {//기능에 해당되는 해당 포인트 로그들 조회
        if(charge!=null&&charge){return pointLogRepository.findByCharge();}//충전
        if(withdraw!=null&&withdraw){return pointLogRepository.findByWithdraw();}//출금
        if(refund!=null&&refund){return pointLogRepository.findByRefund();}//환불
        if(deduct!=null&&deduct){return pointLogRepository.findByDeduct();}//차감
        return pointLogRepository.findAll();}//없을 시 모든 결과 조회

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