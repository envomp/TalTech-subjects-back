package ee.taltech.agonaudid.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenServiceTest {

	@Autowired
	private TokenService tokenService;

	@Test
	public void verifyTokenIsAdmin() {

		String admin = tokenService.createAndSignAdminToken(1);
		String user = tokenService.createAndSignToken(2);

		assert tokenService.verifyTokenIsAdmin(admin);
		assert !tokenService.verifyTokenIsAdmin(user);
		assert tokenService.verifyTokenIsCertainId(admin, 1);
		assert !tokenService.verifyTokenIsAdmin(user) || tokenService.verifyTokenIsCertainId(user, 1);
	}
}
