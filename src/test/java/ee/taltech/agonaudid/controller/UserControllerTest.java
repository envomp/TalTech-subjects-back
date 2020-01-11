package ee.taltech.agonaudid.controller;

import ee.taltech.agonaudid.SpringBootVuejsApplication;
import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.User;
import ee.taltech.agonaudid.pojo.api.users.UserResponseIdToken;
import ee.taltech.agonaudid.pojo.api.subjects.SubjectDto;
import ee.taltech.agonaudid.pojo.api.users.UserDto;
import ee.taltech.agonaudid.repository.SubjectRepository;
import ee.taltech.agonaudid.repository.UserRepository;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = SpringBootVuejsApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Transactional
public class UserControllerTest {

	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private UserRepository userRepository;
	private Subject subject;


	@LocalServerPort
	private int port;

	@Before
	public void init() {
		subject = new Subject("AGO0209", "iti0209 sissejuhatus");
		subjectRepository.save(subject);
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@After
	public void denit() {
		subjectRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	public void addNewUser() {
		String username = "random username";
		UserResponseIdToken user1 = insertUser(username, "password");
		Optional<User> user = userRepository.findByUsername(username);
		assertThat(user.isPresent(), is(true));
		assertThat(user1.getId(), is(user.get().getId()));
	}

	@Test
	public void getUserById() {
		String username = "other username";
		UserResponseIdToken user = insertUser(username, "password");
		UserDto responseUser =
				given()
						.pathParam("id", user.getId())
						.when()
						.get("/api/users/{id}")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.assertThat()
						.extract()
						.as(UserDto.class);

		assertThat(responseUser.getUsername(), is(username));
	}

	@Ignore
	@Test
	public void addUserSubscription() {
		String username = "next username";
		UserResponseIdToken user = insertUser(username, "password");
		String payload = String.format("{\"subjectId\": %d}", subject.getId());
		Long responseId =
				given()
						.pathParam("id", user.getId())
						.when()
						.contentType("application/json")
						.body(payload)
//										.auth().basic(user.getUsername(), "potato")
						.put("/api/users/{id}/subscriptions")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.assertThat()
						.extract()
						.as(Long.class);

		assertThat(responseId, is(subject.getId()));
	}

	@Ignore
	@Test
	public void getUserSubscriptions() {
		subject = new Subject("AGO02092", "iti0209 sissejuhatus");
		subjectRepository.save(subject);
		String username = "next username";
		UserResponseIdToken user = insertUser(username, "password");
		String payload = String.format("{\"subjectId\": %d}", subject.getId());
		System.out.println(subjectRepository.findAll().stream().map(x -> x.getId()).collect(Collectors.toList()));
		Long responseId =
				given()
						.pathParam("id", user.getId())
						.when()
						.contentType("application/json")
						.body(payload)
//										.auth().basic(user.getUsername(), "potato")
						.put("/api/users/{id}/subscriptions")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.assertThat()
						.extract()
						.as(Long.class);
		SubjectDto[] responseIdList =
				given()
						.pathParam("id", user.getId())
						.when()
//										.auth().basic(user.getUsername(), "potato")
						.get("/api/users/{id}/subscriptions")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.assertThat()
						.extract()
						.body()
						.as(SubjectDto[].class);

		assertThat(responseIdList[0].getCode(), is(subject.getCode()));
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