package com.miracle.repository;

import com.miracle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserStringId(String userStringId);
    Optional<User> findByUserPhone(String userPhone);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<User> findFirstByPhoneNumberOrderByUserIdAsc(String phoneNumber);
}