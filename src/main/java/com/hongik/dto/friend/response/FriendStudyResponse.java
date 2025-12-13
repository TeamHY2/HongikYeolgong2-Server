package com.hongik.dto.friend.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class FriendStudyResponse {
	private Long userId;
	private Long friendId;
	private String friendNickname;
	private String studyTime;
	@JsonIgnore// 응답에 안 보이게
	private Duration totalDuration;
}
