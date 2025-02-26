package com.studit.backend.domain.point.service;
import com.studit.backend.domain.point.PointLogType;
import com.studit.backend.domain.point.dto.PointLogRequest;
import com.studit.backend.domain.point.entity.PointLog;
import com.studit.backend.domain.point.repository.PointLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointLogService {//포인트 로그
    private final PointLogRepository pointLogRepository;

    public PointLog createPointLog(PointLogRequest pointLogRequest) {//임시 포인트 로그 생성
        return pointLogRepository.save(PointLogRequest.pointLogForm(pointLogRequest));}

    public PointLog getPointLog(Long pointLogId) {
        return pointLogRepository.findById(pointLogId).orElse(null);}

    public List<PointLog> getPointLogs(Long userId, Long cursor, int pageSize){
        Pageable pageable= PageRequest.of(0, pageSize);//최대 조회 개수 제한
        return pointLogRepository.findNextPointLogs(userId, cursor, pageable);}

    public List<PointLog> getAll() {return pointLogRepository.findAll();}

    public List<PointLog> getEach(PointLogType pointLogType) {
        if(pointLogType!=null){return pointLogRepository.findByPointLogType(pointLogType);}
        return pointLogRepository.findAll();}//없을 시 모든 결과 조회

    public PointLog charge(Long pointLogId,PointLogRequest pointLogRequest, Long userId) {//충전
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));

        long charge=pointLogRequest.getChangePoint();
        saved.setTotalPoint(saved.getTotalPoint()+charge);
        saved.changePointForm(pointLogRequest);
        return pointLogRepository.save(saved);}

    public PointLog withdraw(Long pointLogId,PointLogRequest pointLogRequest, Long userId) {//출금
        //포인트 로그 찾기
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));

        //출금값 가져오기
        long withdraw=pointLogRequest.getChangePoint();

        //출금 처리
        saved.setTotalPoint(saved.getTotalPoint()-withdraw);
        saved.setTotalWithdrawPoint(saved.getTotalWithdrawPoint()+withdraw);
        saved.changePointForm(pointLogRequest);

        //포인트 로그 저장
        return pointLogRepository.save(saved);}

    public PointLog refund(Long pointLogId,PointLogRequest pointLogRequest, Long userId) {//환불
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));

        long refund=pointLogRequest.getChangePoint();
        saved.setTotalPoint(saved.getTotalPoint()+refund);
        saved.changePointForm(pointLogRequest);
        return pointLogRepository.save(saved);}

    public PointLog deduct(Long pointLogId,PointLogRequest pointLogRequest, Long userId) {//차감
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));

        long deduct=pointLogRequest.getChangePoint();
        saved.setTotalPoint(saved.getTotalPoint()-deduct);
        saved.setTotalDeductPoint(saved.getTotalDeductPoint()+deduct);
        saved.changePointForm(pointLogRequest);
        return pointLogRepository.save(saved);}

    public PointLog reward(Long pointLogId, PointLogRequest pointLogRequest, Long userId) {
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));

        long reward=pointLogRequest.getChangePoint();
        saved.setTotalPoint(saved.getTotalPoint()+reward);
        saved.setTotalRewardPoint(saved.getTotalRewardPoint()+reward);
        saved.changePointForm(pointLogRequest);
        return pointLogRepository.save(saved);}}