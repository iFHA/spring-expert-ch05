package dev.fernando.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import dev.fernando.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;

public class ScoreControllerRA {
	private String username, password;
	private String token;
	private Long existingMovieId, nonExistingMovieId;
	private Map<String, Object> scoreInstance;

	@BeforeEach
	void setUp() throws JSONException {
		baseURI = "http://localhost:8080";
		existingMovieId = 3L;
		nonExistingMovieId = 1000L;
		
		username = "alex@gmail.com";
		password = "123456";
		
		token = TokenUtil.obtainAccessToken(username, password);

		scoreInstance = new HashMap<>();
		scoreInstance.put("movieId", existingMovieId);
		scoreInstance.put("score", 2.0);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		scoreInstance.put("movieId", nonExistingMovieId);
		JSONObject score = new JSONObject(scoreInstance);
		given()
			.header("Authorization", "Bearer " + token)
			.body(score)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("scores")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		scoreInstance.remove("movieId");
		JSONObject score = new JSONObject(scoreInstance);
		given()
			.header("Authorization", "Bearer " + token)
			.body(score)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("scores")
		.then()
			.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		scoreInstance.put("score", -1.0F);
		JSONObject score = new JSONObject(scoreInstance);
		given()
			.header("Authorization", "Bearer " + token)
			.body(score)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("scores")
		.then()
			.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());		
	}
}
