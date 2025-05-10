package com.iQuiz.Auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iQuiz.Auth.entity.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String>{
	Optional<UserSession> findByTokenId(String tokenId);
}
