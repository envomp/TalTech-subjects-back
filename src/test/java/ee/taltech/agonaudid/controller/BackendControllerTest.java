package ee.taltech.agonaudid.controller;

import ee.taltech.agonaudid.SpringBootVuejsApplication;
import ee.taltech.agonaudid.pojo.api.users.UserResponseIdToken;
import ee.taltech.agonaudid.pojo.api.users.UserPostDto;
import ee.taltech.agonaudid.repository.UserRepository;
import ee.taltech.agonaudid.service.UserService;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = SpringBootVuejsApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BackendControllerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@LocalServerPort
	private int port;

	@Before
	public void init() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	public void loginWithoutAuth() {
		String username = "nonexistentuser";
		int statusCode =
				given()
						.when()
						.contentType("application/json")
						.param("username", username)
						.get("/api/login")
						.then()
						.extract()
						.statusCode();

		assertThat(statusCode, is(401));
	}

	@Test
	public void loginWithAuth() {
		String username = "username";
		String password = "password";
		Long userId = userService.addUser(new UserPostDto(username, password));
		UserResponseIdToken responseUser =
				given()
						.when()
						.contentType("application/json")
						.param("username", username)
						.auth()
						.basic(username, password)
						.get("/api/login")
						.then()
						.statusCode(is(HttpStatus.SC_OK))
						.extract()
						.body()
						.as(UserResponseIdToken.class);

		assertThat(responseUser.getId(), is(userId));
		userRepository.deleteAll();
	}
}
