package com.funWithStocks.FunWithStocks.repository;

import com.funWithStocks.FunWithStocks.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUsername(String username);
}
