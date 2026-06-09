package com.wsb.module5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wsb.module5.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom 1: Query method - mencari user berdasarkan nama persis
    List<User> findByName(String name);

    // Custom 2: JPQL query - mencari user yang email mengandung string tertentu
    // (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByEmailContaining(@Param("keyword") String keyword);
}
