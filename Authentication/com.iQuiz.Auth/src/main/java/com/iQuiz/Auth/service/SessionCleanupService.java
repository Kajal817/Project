package com.iQuiz.Auth.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.iQuiz.Auth.entity.UserSession;
import com.iQuiz.Auth.repository.UserSessionRepository;

@Service
public class SessionCleanupService {
	private static final Logger logger = LoggerFactory.getLogger(SessionCleanupService.class);
	
	private final UserSessionRepository userSessionRepository;
	
	@Value("${app.jwt.expiration}")
	private long sessionTimeout;
	
	public SessionCleanupService(UserSessionRepository userSessionRepository) {
		this.userSessionRepository = userSessionRepository;
	}
	
	public void cleanUpInactiveSession() {
		logger.info("Starting session Cleanup .....");
		Date currentTime = new Date();
		
//		Checking for each active sessions
		List<UserSession> activeSession = userSessionRepository.findByIsActiveTrue(); 
		
		for(UserSession session: activeSession) {
			if(currentTime.getTime() - session.getLastActivityTime().getTime() > sessionTimeout) {
				session.setActive(false);
				userSessionRepository.save(session);
				logger.info("Deactivated session for user: {}",session.getEmail());
			}
		}
		logger.info("Session Cleanup completed..");
	}
}
