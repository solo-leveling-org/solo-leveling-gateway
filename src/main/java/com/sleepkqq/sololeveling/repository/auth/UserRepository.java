package com.sleepkqq.sololeveling.repository.auth;

import com.sleepkqq.sololeveling.model.auth.User;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
