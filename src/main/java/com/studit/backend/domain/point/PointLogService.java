package com.studit.backend.domain.point;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public List<PointLog> getAll() {return pointLogRepository.findAll();}

    public PointLog charge(Long pointLogId,PointLogRequest pointLogRequest) {//충전
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));
        saved.charge(pointLogRequest);
        return pointLogRepository.save(saved);}

    public PointLog withdraw(Long pointLogId, PointLogRequest pointLogRequest) {//충전
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));
        saved.withdraw(pointLogRequest);
        return pointLogRepository.save(PointLogRequest.pointLogForm(pointLogRequest));}

    public PointLog refund(Long pointLogId, PointLogRequest pointLogRequest) {//차감
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));
        saved.refund(pointLogRequest);
        return pointLogRepository.save(PointLogRequest.pointLogForm(pointLogRequest));}

    public PointLog deduct(Long pointLogId, PointLogRequest pointLogRequest) {//차감
        PointLog saved=pointLogRepository.findById(pointLogId)
                .orElseThrow(()->new RuntimeException("포인트 로그 없음"));
        saved.deduct(pointLogRequest);
        return pointLogRepository.save(PointLogRequest.pointLogForm(pointLogRequest));}}
