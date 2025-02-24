package com.sleepkqq.sololeveling.repository.auth;

import com.sleepkqq.sololeveling.model.auth.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
