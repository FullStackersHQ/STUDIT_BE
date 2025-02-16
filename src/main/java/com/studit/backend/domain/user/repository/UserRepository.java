package com.studit.backend.domain.user.repository;
import com.studit.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByNickname(String nickname);
    User findByKakaoId(Long kakaId);
}
