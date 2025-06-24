package com.iquiz.user.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Table(name = "subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class Subscription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String planName;
	private LocalDate endDate;
	private LocalDate startDate;
	private boolean active;
	
	@PostPersist
	private void afterSubscriptionAdded() {
		log.info("Subscription [{}] activated from {} to {} ", planName, startDate, endDate);
	}
	
}
