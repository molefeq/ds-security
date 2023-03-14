package com.que.ads.ds.security.repository;

import com.que.ads.ds.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("""
                SELECT u from User u WHERE u.userName = :username
            """)
    Optional<User> findUserByUsername(String username);

    @Query("""
                SELECT u from User u WHERE u.userName LIKE ':username%'
            """)
    List<User> findUsersStartingWithUsername(String username);

    @Query("""
                SELECT u from User u WHERE u.forgotPasswordKey = :forgotPasswordKey
            """)
    Optional<User> findUserByForgotPasswordKey(String forgotPasswordKey);

    @Query("""
                SELECT u from User u WHERE u.emailAddress = :emailAddress
            """)
    Optional<User> findUserByEmailAddress(String emailAddress);
}

