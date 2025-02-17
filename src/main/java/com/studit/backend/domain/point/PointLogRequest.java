package com.studit.backend.domain.point;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PointLogRequest {//포인트 로그
    private Long userId;
    private Long roomId;
    private Long paymentId;

    private Long changePoint;//변동 포인트
    private Long totalWithdrawPoint;//총 출금 포인트
    private Long totalRewardPoint;//총 보상 포인트

    private Long totalDeductPoint;//총 차감 포인트
    private Long totalPoint;//총 포인트
    private PointLogType pointLogType;//포인트 로그 종류

    public static PointLog pointLogForm(PointLogRequest pointLogRequest){
        return PointLog.builder().userId(pointLogRequest.getUserId())
                .roomId(pointLogRequest.getRoomId())
                .paymentId(pointLogRequest.getPaymentId())

                .totalWithdrawPoint(pointLogRequest.getTotalWithdrawPoint())
                .totalRewardPoint(pointLogRequest.getTotalRewardPoint())
                .totalDeductPoint(pointLogRequest.getTotalDeductPoint())
                .totalPoint(pointLogRequest.getTotalPoint())
                .pointLogType(pointLogRequest.getPointLogType()).build();}}