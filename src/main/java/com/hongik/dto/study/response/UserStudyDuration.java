package com.hongik.dto.study.response;

import java.time.LocalDateTime;

public interface UserStudyDuration {
	Long getUserId();
	String getNickname();
	boolean getStudyStatus();
	int getTotalSeconds();
	LocalDateTime getLatestCreatedAt();
}
