package ee.taltech.agonaudid.controller;

import ee.taltech.agonaudid.pojo.api.users.UserResponseIdToken;
import ee.taltech.agonaudid.service.TokenService;
import ee.taltech.agonaudid.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class BackendController {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final UserService userService;
	private final TokenService tokenService;

	public BackendController(UserService userService, TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/login")
	public UserResponseIdToken getHome(@RequestParam() String username) {
		LOG.info(String.format("Getting ID for user %s", username));
		return tokenService.createResponse(userService.getHome(username));
	}
}
