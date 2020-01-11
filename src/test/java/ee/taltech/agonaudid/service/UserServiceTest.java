package ee.taltech.agonaudid.service;

import ee.taltech.agonaudid.pojo.api.users.UserPostDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	public void addSubjectAsSubscriptionAndVerifyItExists() {
		long userId = userService.addUser(new UserPostDto("user", "pass"));
		userService.addSubscription(userId, 1L);

		assert userService.getSubscriptions(userId).size() == 1;
	}
}
