package ee.taltech.agonaudid.repository;

import ee.taltech.agonaudid.pojo.api.users.UserPostDto;
import ee.taltech.agonaudid.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

  @Bean
  CommandLineRunner initUserDatabase(UserService userService) {
    return args -> {
    	if (!userService.getAllUsers().stream().anyMatch(x -> x.getUsername().equals("agonaudid"))) {
		    userService.addSuperUser(new UserPostDto("agonaudid", "team4"));
	    }
    };
  }
}
