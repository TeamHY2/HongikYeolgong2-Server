package com.hongik.dto.study.response;

import java.time.LocalDateTime;

public interface UserStudyDuration {
	Long getUserId();
	String getUserName();
	int getStudyStatus();
	int getTotalSeconds();
	LocalDateTime getLatestCreatedAt();
}
