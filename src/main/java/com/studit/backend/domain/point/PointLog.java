package com.studit.backend.domain.point;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class PointLog {
    @Id //포인트 로그
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointLogId; //포인트 로그 ID
    private Long userId; //회원 ID
    private Long roomId; //스터디룸 ID
    private Long paymentId; //결제 ID

    private Long charge; //충전 포인트
    private Long withdraw; //출금 포인트
    private Long refund; //환불 포인트
    private Long deduct; //차감 포인트
    private Long totalPoint;//최종 포인트

    public void charge(PointLogRequest pointLogRequest){
        this.charge=pointLogRequest.getCharge();}

    public void withdraw(PointLogRequest pointLogRequest){
        this.withdraw=pointLogRequest.getCharge();}

    public void refund(PointLogRequest pointLogRequest){
        this.refund=pointLogRequest.getCharge();}

    public void deduct(PointLogRequest pointLogRequest){
        this.deduct=pointLogRequest.getCharge();}}

//private PointLogType pointType;//포인트 타입