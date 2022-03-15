package com.cscu9yw.eventregistrationbackend.repository;

import com.cscu9yw.eventregistrationbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUid(String uid);
    boolean existsByUid(String uid);

}
