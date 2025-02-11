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

    private Long charge;
    private Long withdraw;
    private Long refund;
    private Long deduct;
    private Long totalPoint;
    //private PointLogType pointLogType;

    public static PointLog pointLogForm(PointLogRequest pointLogRequest){
        return PointLog.builder().userId(pointLogRequest.getUserId())
                .roomId(pointLogRequest.getRoomId())
                .paymentId(pointLogRequest.getPaymentId())
                .totalPoint(pointLogRequest.getTotalPoint()).build();}}