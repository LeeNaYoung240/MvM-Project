package com.sparta.mvm.AuthTest;

import com.sparta.mvm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestUserRepository extends JpaRepository<TestUser, Long> {
    TestUser findByUsername(String username);
}
