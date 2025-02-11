package com.studit.backend.domain.point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog,Long> {
    @Query("SELECT p FROM PointLog p WHERE p.charge>0")
    List<PointLog> findByCharge();//충전

    @Query("SELECT p FROM PointLog p WHERE p.withdraw>0")
    List<PointLog> findByWithdraw();//충전

    @Query("SELECT p FROM PointLog p WHERE p.refund>0")
    List<PointLog> findByRefund();//충전

    @Query("SELECT p FROM PointLog p WHERE p.deduct>0")
    List<PointLog> findByDeduct();}//충전