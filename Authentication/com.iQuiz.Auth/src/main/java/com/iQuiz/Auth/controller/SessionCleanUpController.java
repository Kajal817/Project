package com.iQuiz.Auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iQuiz.Auth.service.SessionCleanupService;

@RestController
@RequestMapping("/session")
public class SessionCleanUpController {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionCleanUpController.class);
	private final SessionCleanupService sessionCleanupService;
	
	@Autowired
	public SessionCleanUpController(SessionCleanupService sessionCleanupService) {
		this.sessionCleanupService = sessionCleanupService;
	}
	
	@PostMapping("/cleanup")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> cleanUpInactiveSession(){
		logger.info("Manual session cleanup triggered.");
		sessionCleanupService.cleanUpInactiveSession();
		return ResponseEntity.ok("Inactive sessions cleaned up successfully.");
	}
}
