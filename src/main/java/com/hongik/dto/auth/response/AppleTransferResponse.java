package com.hongik.dto.auth.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppleTransferResponse {
	private String sub;
	@JsonProperty("transfer_sub")
	private String transferSub;
}
