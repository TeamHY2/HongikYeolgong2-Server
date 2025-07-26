package com.hongik.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppleUserInfoResponse {
	private String sub;
	private String email;
	@JsonProperty("is_private_email")
	private boolean isPrivateEmail;
}
