package ee.taltech.agonaudid.controller;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ee.taltech.agonaudid.domain.Role;
import ee.taltech.agonaudid.pojo.api.users.UserDto;
import ee.taltech.agonaudid.pojo.api.users.UserPostDto;
import ee.taltech.agonaudid.pojo.api.users.UserResponseIdToken;
import ee.taltech.agonaudid.pojo.api.users.UserSubscriptionDto;
import ee.taltech.agonaudid.service.TokenService;
import ee.taltech.agonaudid.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@RestController()
@RequestMapping("/api/users")
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final UserService userService;

	private final TokenService tokenService;

	public UserController(UserService userService, TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "")
	public UserResponseIdToken addNewUser(@RequestBody UserPostDto user) {
		LOG.info("Registering an user with username {}.", user.getUsername());
		long id = userService.addUser(user);
		return tokenService.createResponse(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/{id}")
	public UserDto getUserById(@PathVariable("id") long id) {
		LOG.info("Getting an user with id {}.", id);
		return new UserDto(userService.getUser(id));
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/{id}/subscriptions")
	public List<Long> getUserSubscriptions(@PathVariable("id") long id) {
		return userService.getSubscriptions(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/all")
	public List<UserDto> getAllUsers() {
		return userService.getAllUsers().stream().map(UserDto::new).collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(path = "/{id}")
	public void deleteUserById(@PathVariable("id") long id, @RequestBody UserResponseIdToken token) throws AuthenticationException {
		if ((tokenService.verifyTokenIsAdmin(token.getToken()) || tokenService.verifyTokenIsCertainId(token.getToken(), id)) && !userService.getUser(id).getRole().equals(Role.ADMIN)) {
			LOG.info("Deleting user");
			userService.removeUser(userService.getUser(id).getUsername());
		} else {
			throw new AuthenticationException("You are not permitted.");
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping(path = "/{id}/subscriptions")
	public Long addUserSubscription(@PathVariable("id") long id, @RequestBody UserSubscriptionDto sub) throws AuthenticationException {
		if (tokenService.verifyTokenIsCertainId(sub.getToken(), id)) {
			return userService.addSubscription(id, sub.getSubjectId());
		} else {
			throw new AuthenticationException("You lack permissions to add subscriptions to that user.");
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}/subscriptions")
	public void deleteUserSubscription(@PathVariable("id") long id, @RequestBody UserSubscriptionDto sub) throws AuthenticationException {
		if (tokenService.verifyTokenIsCertainId(sub.getToken(), id)) {
			userService.removeSubscription(id, sub.getSubjectId());
		} else {
			throw new AuthenticationException("You lack permissions to delete subscriptions for that user.");
		}
	}

}
