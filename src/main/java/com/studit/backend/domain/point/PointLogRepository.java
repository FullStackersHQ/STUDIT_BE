package com.studit.backend.domain.point;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog,Long> {

    //특정 사용자(userId)의 포인트 로그를 커서 기준으로 한 번에 10개 조회
    @Query("SELECT p FROM PointLog p WHERE p.userId= :userId AND " +
            "p.pointLogId > :cursor ORDER BY p.pointLogId ASC")
    List<PointLog> findNextPointLogs(@Param("userId") Long userId,
    @Param("cursor") Long cursor, Pageable pageable);

    //특징 포인트 로그타입(CHARGE,WITHDRAW 등)에 해당하는 데이터 조회
    List<PointLog> findByPointLogType(PointLogType pointLogType);}