package com.hongik.service.auth.apple;

import com.hongik.domain.user.SocialPlatform;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.auth.response.AppleTransferResponse;
import com.hongik.dto.auth.response.AppleUserInfoResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppleMigrationService {

	private final UserRepository userRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	private static final String APPLE_AUTH_URL = "https://appleid.apple.com";
	private static final String TOKEN_URL = APPLE_AUTH_URL + "/auth/token";
	private static final String MIGRATION_URL = APPLE_AUTH_URL + "/auth/usermigrationinfo";

	@Value("${apple.old.key-id}") private String OLD_KEY_ID;
	@Value("${apple.old.client-id}") private String OLD_CLIENT_ID;
	@Value("${apple.old.key-path}") private String OLD_KEY_PATH;
	@Value("${apple.old.team-id}") private String OLD_TEAM_ID;

	@Value("${apple.new.client-id}") private String NEW_CLIENT_ID;
	@Value("${apple.new.key-id}") private String NEW_KEY_ID;
	@Value("${apple.new.key-path}") private String NEW_KEY_PATH;
	@Value("${apple.new.team-id}") private String NEW_TEAM_ID;

	public void migrateAllUsers() throws IOException {
		List<User> users = userRepository.findAllBySubIsNotNullAndSocialPlatform(SocialPlatform.APPLE);

		String oldClientSecret = createClientSecret(OLD_KEY_PATH, OLD_TEAM_ID, OLD_KEY_ID, OLD_CLIENT_ID);
		String accessToken = requestAccessToken(OLD_CLIENT_ID, oldClientSecret);

		migrateToTransferSub(users, oldClientSecret, accessToken);
		migrateToNewSub(users, accessToken);
	}

	private void migrateToTransferSub(List<User> users, String clientSecret, String accessToken) {
		for (User user : users) {
			try {
				String transferSub = requestTransferSub(user.getSub(), clientSecret, accessToken);
				user.updateAppleTransferSub(transferSub);
				userRepository.save(user);
			} catch (Exception e) {
				logMigrationError("1단계", user.getId(), e);
			}
		}
	}

	private void migrateToNewSub(List<User> users, String oldAccessToken) {
		for (User user : users) {
			try {
				String newSub = requestNewSub(user.getAppleTransferSub(), oldAccessToken);
				user.updateNewSub(newSub);
				userRepository.save(user);
			} catch (Exception e) {
				logMigrationError("2단계", user.getId(), e);
			}
		}
	}

	private String requestTransferSub(String userIdentifier, String clientSecret, String accessToken) {
		HttpEntity<MultiValueMap<String, String>> request = buildMigrationRequest(
				Map.of(
						"client_id", OLD_CLIENT_ID,
						"client_secret", clientSecret,
						"sub", userIdentifier,
						"target", NEW_TEAM_ID
				),
				accessToken
		);

		ResponseEntity<AppleTransferResponse> response = restTemplate.postForEntity(MIGRATION_URL, request, AppleTransferResponse.class);
		return Optional.ofNullable(response.getBody())
				.map(AppleTransferResponse::getTransferSub)
				.orElseThrow(() -> new IllegalArgumentException("transfer_sub 발급 실패"));
	}

	private String requestNewSub(String transferSub, String oldAccessToken) throws IOException {
		String newClientSecret = createClientSecret(NEW_KEY_PATH, NEW_TEAM_ID, NEW_KEY_ID, NEW_CLIENT_ID);

		HttpEntity<MultiValueMap<String, String>> request = buildMigrationRequest(
				Map.of(
						"client_id", NEW_CLIENT_ID,
						"client_secret", newClientSecret,
						"transfer_sub", transferSub
				),
				oldAccessToken
		);

		ResponseEntity<AppleUserInfoResponse> response = restTemplate.postForEntity(MIGRATION_URL, request, AppleUserInfoResponse.class);
		return Optional.ofNullable(response.getBody())
				.map(AppleUserInfoResponse::getSub)
				.orElseThrow(() -> new IllegalArgumentException("user info 조회 실패"));
	}

	private String requestAccessToken(String clientId, String clientSecret) {
		HttpEntity<MultiValueMap<String, String>> request = buildFormRequest(
				Map.of(
						"grant_type", "client_credentials",
						"scope", "user.migration",
						"client_id", clientId,
						"client_secret", clientSecret
				)
		);

		ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);
		return Optional.ofNullable((String) response.getBody().get("access_token"))
				.orElseThrow(() -> new IllegalArgumentException("access_token 발급 실패: " + response.getBody()));
	}

	private String createClientSecret(String keyPath, String teamId, String keyId, String clientId) throws IOException {
		Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
				.setHeaderParam("kid", keyId)
				.setHeaderParam("alg", "ES256")
				.setIssuer(teamId)
				.setIssuedAt(new Date())
				.setExpiration(expirationDate)
				.setAudience(APPLE_AUTH_URL)
				.setSubject(clientId)
				.signWith(SignatureAlgorithm.ES256, getPrivateKey(keyPath))
				.compact();
	}

/*	private PrivateKey getPrivateKey(String keyPath) throws IOException {
		Resource resource = new FileSystemResource(keyPath);
		String privateKeyPem = new String(Files.readAllBytes(Paths.get(resource.getURI())));
		try (PEMParser pemParser = new PEMParser(new StringReader(privateKeyPem))) {
			PrivateKeyInfo keyInfo = (PrivateKeyInfo) pemParser.readObject();
			return new JcaPEMKeyConverter().getPrivateKey(keyInfo);
		}
	}*/

	private PrivateKey getPrivateKey(String keyFileName) throws IOException {
		// 1. classpath에서 파일을 읽어옴
		Resource resource = new ClassPathResource(keyFileName);

		// 2. InputStream으로부터 PEM 문자열을 읽음
		String privateKeyPem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

		try (PEMParser pemParser = new PEMParser(new StringReader(privateKeyPem))) {
			PrivateKeyInfo keyInfo = (PrivateKeyInfo) pemParser.readObject();
			return new JcaPEMKeyConverter().getPrivateKey(keyInfo);
		}
	}

	private HttpEntity<MultiValueMap<String, String>> buildMigrationRequest(Map<String, String> params, String bearerToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBearerAuth(bearerToken);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.setAll(params);

		return new HttpEntity<>(body, headers);
	}

	private HttpEntity<MultiValueMap<String, String>> buildFormRequest(Map<String, String> params) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.setAll(params);

		return new HttpEntity<>(body, headers);
	}

	private void logMigrationError(String step, Long userId, Exception e) {
		System.err.printf("%s 실패 - userId: %d => %s%n", step, userId, e.getMessage());
	}
}
