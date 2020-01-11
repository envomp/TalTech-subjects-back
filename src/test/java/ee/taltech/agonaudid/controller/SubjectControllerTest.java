package ee.taltech.agonaudid.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.agonaudid.SpringBootVuejsApplication;
import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.SubjectRating;
import ee.taltech.agonaudid.pojo.api.users.UserResponseIdToken;
import ee.taltech.agonaudid.pojo.api.comments.SubjectCommentDto;
import ee.taltech.agonaudid.pojo.api.comments.SubjectCommentPostDto;
import ee.taltech.agonaudid.pojo.api.subjects.SubjectDto;
import ee.taltech.agonaudid.pojo.api.reviews.SubjectReviewDto;
import ee.taltech.agonaudid.pojo.api.reviews.SubjectReviewPostDto;
import ee.taltech.agonaudid.repository.SubjectRepository;
import ee.taltech.agonaudid.service.TokenService;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = SpringBootVuejsApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SubjectControllerTest {

	@Autowired
	private SubjectRepository subjectRepo;
	private Subject subject;

	@Autowired
	private TokenService tokenService;
	private ObjectMapper objectMapper = new ObjectMapper();

	@LocalServerPort
	private int port;

	@Before
	public void init() {
		subject = new Subject("AGO0209", "iti0209 sissejuhatus");
		subjectRepo.save(subject);
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@After
	public void denit() {
		subjectRepo.deleteAll();
	}

	@Test
	public void getSubjectsNoParams() {
		Long[] subjects =
				given()
						.when()
						.get("/api/subjects")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.extract()
						.body()
						.as(Long[].class);

		assertThat(subjects, arrayContaining(subject.getId()));
	}

	@Test
	public void getSubjectsWithParams() {
		Subject[] subjects =
				given()
						.when()
						.param("q", 1)
						.get("/api/subjects")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.extract()
						.body()
						.as(Subject[].class);

		assertThat(subjects[0].getId(), is(subject.getId()));
	}

	@Test
	public void getNonexistentSubjectsWithParams() {
		Subject[] subjects =
				given()
						.when()
						.param("q", 1)
						.param("ids", 100)
						.get("/api/subjects")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.extract()
						.body()
						.as(Subject[].class);

		assertThat(subjects.length, is(0));
	}

	@Test
	public void getSubjectByCode() {
		SubjectDto subjectDto =
				given()
						.when()
						.pathParam("id", subject.getId())
						.get("/api/subjects/{id}")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.extract()
						.body()
						.as(SubjectDto.class);

		assertThat(subjectDto.getCode(), is(subject.getCode()));
		assertThat(subjectDto.getId(), is(subject.getId()));
	}

	@Test
	public void addNewCommentAndGetSubjectComments() throws JsonProcessingException {
		String username = "agofan11";
		UserResponseIdToken user = insertUser(username, "password");

		SubjectCommentPostDto post = new SubjectCommentPostDto(user.getId(), "comment", tokenService.createAndSignToken(user.getId()));
		String payload = objectMapper.writeValueAsString(post);

		Long subjectCommentId =
				given()
						.when()
						.pathParam("id", subject.getId())
						.contentType("application/json")
						.body(payload)
						.post("/api/subjects/{id}/comments")
						.then()
						.statusCode(is(HttpStatus.SC_CREATED))
						.extract()
						.body()
						.as(Long.class);


		SubjectCommentDto[] responseComments =
				given()
						.pathParam("id", subject.getId())
						.when()
						.get("/api/subjects/{id}/comments")
						.then()
						.statusCode(HttpStatus.SC_OK)
						.assertThat()
						.extract()
						.as(SubjectCommentDto[].class);

		assertThat(responseComments[0].getId(), is(subjectCommentId));
	}

	@Test
	public void addNewReviewAndGetSubjectReviews() throws JsonProcessingException {
		String username = "agofan10";
		UserResponseIdToken user = insertUser(username, "password");

		SubjectReviewPostDto post = new SubjectReviewPostDto(user.getId(), "review", SubjectRating.POSITIVE, tokenService.createAndSignToken(user.getId()));
		String payload = objectMapper.writeValueAsString(post);

		Long subjectReviewId =
				given()
						.pathParam("id", subject.getId())
						.when()
						.contentType("application/json")
						.body(payload)
						.post("/api/subjects/{id}/reviews")
						.then()
						.statusCode(is(HttpStatus.SC_CREATED))
						.extract()
						.body()
						.as(Long.class);

		SubjectReviewDto[] responseReview =
				given()
						.pathParam("id", subject.getId())
						.when()
						.get("/api/subjects/{id}/reviews")
						.then()
						.statusCode(HttpStatus.SC_OK)
						.assertThat()
						.extract()
						.as(SubjectReviewDto[].class);

		assertThat(responseReview[0].getId(), is(subjectReviewId));
	}

	private UserResponseIdToken insertUser(String username, String password) {
		String registerPayload = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
		return given()
				.when()
				.contentType("application/json")
				.body(registerPayload)
				.post("/api/users")
				.then()
				.statusCode(is(HttpStatus.SC_CREATED))
				.extract()
				.body()
				.as(UserResponseIdToken.class);
	}

}